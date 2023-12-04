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
    List<String[]> plain_lyric = new ArrayList<>();
    String timestamped_lyric;
    List<Map<String, Double>> timestamped_lines;


    private void loadPlain(String path) {
        try {
            Path file = Paths.get(path);
            List<String> lines = Files.readAllLines(file, StandardCharsets.UTF_8);
            for (String line : lines) {
                String[] array = line.split("\n");
                plain_lyric.add(array);
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

    private void agregateLines()
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
    private String[] seperateWords(String line){
        return line.split(" ");
    }
    // DO ZMIANY NA MAP
    public void matchWords(Map<String, Double> speculatedText, String actualText){
        ;
    }
}
