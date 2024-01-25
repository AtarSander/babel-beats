package beats.babel.babelbeats;
import java.io.*;
import java.util.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;


public class Timestamper {
    List<String[]> plain_lyric_words = new ArrayList<>();
    List<String> plain_lyric_lines = new ArrayList<>();
    private List<String> chosenLyrics = new ArrayList<>();
    private String timestamped_lyric;
    private String language;
    List<Map<String, Double>> timestamped_lines = new ArrayList<>();
    double accuracy;
    private final static String plainPath = "src/main/resources/lyrics/plainLyrics/";


    public List<String> matchTranslated(String targetLang, String srcLang){
        List<String> mutableList = new ArrayList<>(chosenLyrics);

        mutableList.removeIf(String::isEmpty);

        DeepLHandler dl = new DeepLHandler();
        return dl.translate(mutableList, targetLang, srcLang);
    }

    public JSONObject saveTimestamps(String name, long size, String targetLang, String srcLang)  throws Exception {
        List<Pair> lyrics = getTimestamps(name);
        if (chosenLyrics.size() > 200)
            throw new Exception("Wrong plain text");
        JSONObject record = new JSONObject();
        System.out.println(accuracy);
        if (accuracy < 0.3)
            throw new Exception("Accuracy too low");
        JSONArray timestampsJsonArray = new JSONArray();
        JSONArray translatedJsonArray = new JSONArray();

        List<String> translatedLines = matchTranslated(targetLang, srcLang);
//        List<String> translatedLines = Arrays.asList(test.split("\\. "));
        assert(translatedLines.size() == lyrics.size());
        for (int i = 0; i < Math.min(lyrics.size(), translatedLines.size()); i++) {
            Pair pair = lyrics.get(i);
            JSONObject pairObjectTimestamp = new JSONObject();
            JSONObject pairObjectTranslate = new JSONObject();
            pairObjectTimestamp.put("key", pair.getKey());
            pairObjectTimestamp.put("value", pair.getValue());

            pairObjectTranslate.put("key", translatedLines.get(i));
            pairObjectTranslate.put("value", pair.getValue());
            timestampsJsonArray.put(pairObjectTimestamp);
            translatedJsonArray.put(pairObjectTranslate);
        }
        record.put("_id", size+1);
        record.put("title", name);
        record.put("timestamps", timestampsJsonArray);
        record.put("timestampsTranslated", translatedJsonArray);
//        existingData.put(record);
//        writeJsonToFile(existingData, "src/main/resources/lyrics/processedLyrics/processedSong.json");

        return record;
    }

    private static JSONArray readJsonFromFile(String fileName) {
        try (FileReader fileReader = new FileReader(fileName)) {
            return new JSONArray(new JSONTokener(fileReader));
        } catch (IOException e) {
            return new JSONArray();
        }
    }

    private static void writeJsonToFile(JSONArray data, String fileName) {
        try (FileWriter fileWriter = new FileWriter(fileName)) {
            fileWriter.write(data.toString(2));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected List<Pair> getTimestamps(String name) {
        Vector<List<String>> versionsLyrics = loadPlain(name);
        callWhisper(name);
        List<Pair> lyricsTemp = new ArrayList<>();
        loadTimestamped("src/main/resources/lyrics/timestamps/" + name + ".json");
        aggregateLines();
        List<Pair> finalLyrics = new ArrayList<>();
        double max_accuracy = 0.;
        for (int i = 0; i<versionsLyrics.size(); i++) {
            List<String> tempJavaIsShit = new ArrayList<>(versionsLyrics.get(i));
            tempJavaIsShit.removeIf(String::isEmpty);
            plain_lyric_lines = tempJavaIsShit;
            plain_lyric_words.clear();
            for (String line : plain_lyric_lines) {
                plain_lyric_words.add(separateWords(line));
            }
            lyricsTemp = timestamp();
            if (accuracy > max_accuracy) {
                max_accuracy = accuracy;
                finalLyrics = lyricsTemp;
                chosenLyrics = versionsLyrics.get(i);
            }
        }
        accuracy = max_accuracy;
        return correctTimestamps(finalLyrics);
    }

    private void callWhisper(String name) {
        ProcessBuilder pb = new ProcessBuilder("python", "src/main/python/transcript.py", name, language);
        pb.redirectErrorStream(true);

        try {
            Process process = pb.start();

            // Redirect the output to the console
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    System.out.println(line);
                }
            }

            // Wait for the process to finish
            int exitCode = process.waitFor();
            System.out.println("Whisper Exit Code: " + exitCode);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private Vector<List<String>> loadPlain(String name) {
        Vector<List<String>> plain_lines = new Vector<>();
        Path filePath = Paths.get(plainPath + name + ".json");
        try
        {
            String jsonString = new String(Files.readAllBytes(Paths.get(plainPath + name + ".json")));
            JSONArray jsonArray = new JSONArray(jsonString);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);
                plain_lines.add(Arrays.asList(obj.getString("lyrics").split("\n")));
                language = obj.getString("language");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

//        try
//        {
//            Files.delete(filePath);
//            System.out.println("The file " + plainPath + name + ".json" + " has been successfully deleted.");
//        }
//        catch (IOException e) {
//            System.err.println("Error: Unable to delete the file " + plainPath + name + ".json");
//            e.printStackTrace();
//        }
        return plain_lines;
    }

    private void loadTimestamped(String path)
    {
        Path filePath = Paths.get(path);
        try {
            BufferedReader reader = new BufferedReader(new FileReader(path));
            StringBuilder jsonString = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                jsonString.append(line);
            }
            timestamped_lyric = jsonString.toString();

        } catch (IOException e) {
            e.printStackTrace();
        }

//        try
//        {
//            Files.delete(filePath);
//            System.out.println("The file " + path + " has been successfully deleted.");
//        }
//        catch (IOException e) {
//            System.err.println("Error: Unable to delete the file " + path + ".json");
//            e.printStackTrace();
//        }
    }

    private Map<String, Double> mapTimestampsLine(String timestamped_line) {
        Map<String, Double> timestamped_mapped = new HashMap<>();
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(timestamped_line);
            JsonNode wordsNode = jsonNode.path("words");
            for (JsonNode wordNode : wordsNode) {
                String text = wordNode.path("text").asText();
                double start = wordNode.path("start").asDouble();

                timestamped_mapped.put(text, start);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return timestamped_mapped;
    }

    private void aggregateLines() {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(timestamped_lyric);
            JsonNode lines = jsonNode.path("segments");
            for (JsonNode line : lines) {
                timestamped_lines.add(mapTimestampsLine(line.toString()));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String[] separateWords(String line) {
        return line.split(" ");
    }

    private List<String> sortKeys(Map<String, Double> map) {
        List<Map.Entry<String, Double>> entryList = new ArrayList<>(map.entrySet());
        entryList.sort(Map.Entry.comparingByValue());
        List<String> sortedKeys = new ArrayList<>();
        for (Map.Entry<String, Double> entry : entryList) {
            sortedKeys.add(entry.getKey());
        }
        return sortedKeys;
    }

    private List<Pair> timestamp() {
        int i = 0, j, k, l, count, max_count;
        double saved = 0., max_saved = 0., previous = -1., temp_accuracy=0., total_accuracy=0.;
        String[] line_plain;
        Map<String, Double> line_stamped;
        List<Pair> final_lyric = new ArrayList<>();
        while (i < plain_lyric_words.size()) {
            k = 0;
            max_count = 0;
            if (i < plain_lyric_words.size() - 1 && i < timestamped_lines.size() - 1)
                l = i + 1;
            else
                l = timestamped_lines.size();
            while (k < l) {
                line_plain = plain_lyric_words.get(i);
                line_stamped = timestamped_lines.get(k);
                List<String> mapKeysList = sortKeys(line_stamped);
                j = 0;
                count = 0;
                while (j < line_plain.length && j < mapKeysList.size()) {
                    for (String entry : mapKeysList) {
                        if ((line_plain[j]).equalsIgnoreCase(entry)) {
                            count++;
                            if (count < 2)
                                saved = line_stamped.get(entry);
                            break;
                        }
                    }
                    j++;
                }
                if (count > max_count && saved > previous) {
                    max_count = count;
                    max_saved = saved;
                    temp_accuracy = (double) count/line_plain.length;
                }
                k++;
            }
            total_accuracy += temp_accuracy;
            final_lyric.add(new Pair(plain_lyric_lines.get(i), max_saved * 1000));
            previous = max_saved;
            i++;

        }
        accuracy = total_accuracy/plain_lyric_words.size();
        return final_lyric;
    }

    private  List<Pair> correctTimestamps(List<Pair> finalLyrics) {
        finalLyrics.removeIf(pair -> pair.getKey().isEmpty());
        Pair prev = finalLyrics.get(0);
        Pair next, current = finalLyrics.get(1);
        double timeBetween=0., counter=0., result;
        for (int i = 1; i < finalLyrics.size(); i++)
        {
            current = finalLyrics.get(i);
            if (current.getValue() > prev.getValue() && current.getValue() - prev.getValue() < 5000)
            {
                timeBetween += current.getValue() - prev.getValue();
                counter++;
            }
            prev = current;
        }
        timeBetween /= counter;
        if (timeBetween == 0.)
            timeBetween = 1000.;
        prev = finalLyrics.get(0);
        for (int i = 1; i < finalLyrics.size()-1; i++)
        {
            current = finalLyrics.get(i);
            next = finalLyrics.get(i+1);
            if (current.getValue() <= prev.getValue() && (prev.getValue()+timeBetween < next.getValue() || next.getValue() == current.getValue()))
            {
                result = Math.round((prev.getValue()+timeBetween) * 100)/100.0;
                finalLyrics.set(i, new Pair(current.getKey(), result));
                current = finalLyrics.get(i);
            }
            else if (current.getValue() <= prev.getValue() && prev.getValue()+timeBetween > next.getValue() && next.getValue() != current.getValue())
            {
                double timeBetween2 = Math.round((next.getValue() + prev.getValue()/2) * 100)/100.0;
                finalLyrics.set(i, new Pair(current.getKey(), timeBetween2));
                current = finalLyrics.get(i);
            }
            prev = current;
        }
        if (finalLyrics.getLast().getValue() <= current.getValue())
        {
            result = Math.round(((current.getValue()+timeBetween)*100)/100.0);
            finalLyrics.set(finalLyrics.size()-1, new Pair(finalLyrics.getLast().getKey(), result));
        }
        return finalLyrics;
    }

    public double getAccuracy()
    {
        return this.accuracy;
    }
}
