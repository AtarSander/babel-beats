package beats.babel.babelbeats;
import java.io.BufferedReader;
import java.io.FileReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.nio.charset.StandardCharsets;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Timestamper {
    List<String[]> plain_lyric_words = new ArrayList<>();
    List<String> plain_lyric_lines = new ArrayList<>();
    String timestamped_lyric;
    List<Map<String, Double>> timestamped_lines = new ArrayList<>();

    public List<Pair> getTimestamps(String name)
    {
        loadPlain("src/main/resources/lyrics/plain/" + name + ".txt");
        loadTimestamped("src/main/resources/lyrics/timestamped/" + name + ".json");
        aggregateLines();
        return timestamp();
    }


    private void loadPlain(String path) {
        try {
            Path file = Paths.get(path);
            List<String> lines = Files.readAllLines(file, StandardCharsets.UTF_8);
            for (String line : lines) {
                plain_lyric_lines.add(line);
                plain_lyric_words.add(separateWords(line));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadTimestamped(String path)
    {
        try
        {
            BufferedReader reader = new BufferedReader(new FileReader(path));
            StringBuilder jsonString = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                jsonString.append(line);
            }
            timestamped_lyric = jsonString.toString();

        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Map<String, Double> mapTimestampsLine(String timestamped_line)
    {
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

        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return timestamped_mapped;
    }

    private void aggregateLines()
    {
        try
        {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(timestamped_lyric);
            JsonNode lines = jsonNode.path("segments");
            for (JsonNode line : lines)
            {
                timestamped_lines.add(mapTimestampsLine(line.toString()));
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    private String[] separateWords(String line){
        return line.split(" ");
    }

    private List<String> sortKeys(Map<String, Double> map)
    {
        List<Map.Entry<String, Double>> entryList = new ArrayList<>(map.entrySet());
        entryList.sort(Map.Entry.comparingByValue());
        List<String> sortedKeys = new ArrayList<>();
        for (Map.Entry<String, Double> entry : entryList) {
            sortedKeys.add(entry.getKey());
        }
        return sortedKeys;
    }

    private List<Pair> timestamp()
    {
        int i=0, j, k=0, l=0, count, max_count;
        Double saved = 0., max_saved = 0., previous= -1.;
        String[] line_plain;
        Map<String, Double> line_stamped;
        List<Pair> final_lyric = new ArrayList<>();
        while (i < plain_lyric_words.size())
        {
            k = 0;
            max_count = 0;
            if (i < plain_lyric_words.size() - 1 && i < timestamped_lines.size() - 1)
                l = i + 1;
            else
                l = timestamped_lines.size();
            while (k < l)
            {
                line_plain = plain_lyric_words.get(i);
                line_stamped = timestamped_lines.get(k);
                List<String> mapKeysList = sortKeys(line_stamped);
                j = 0;
                count = 0;
                while (j < line_plain.length && j < mapKeysList.size())
                {
                    for (String entry : mapKeysList)
                    {
                        if ((line_plain[j]).equalsIgnoreCase(entry))
                        {
                            count++;
                            if (count < 2)
                                saved = line_stamped.get(entry);
                            break;
                        }
                    }

                    j++;
                }
                if (count > max_count && saved > previous)
                {
                    max_count = count;
                    max_saved = saved;
                }
                k++;
            }
            final_lyric.add(new Pair(plain_lyric_lines.get(i), max_saved));
            previous = max_saved;
            i++;
        }
        return final_lyric;
    }
}

class Pair {
    private String key;
    private double value;

    public Pair(String key, double value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public double getValue() {
        return value;
    }
}

