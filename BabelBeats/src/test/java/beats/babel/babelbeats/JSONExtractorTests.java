package beats.babel.babelbeats;
import org.json.JSONException;
import org.junit.jupiter.api.Test;
import org.json.JSONArray;
import java.util.ArrayList;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;


public class JSONExtractorTests {
    @Test
    public void testExtractString() {
        String json = "{\"key\": \"value\"}";
        JSONExtractor jsonExtractor = new JSONExtractor();
        String result = jsonExtractor.extract(json, "key");

        assertThat(result).isEqualTo("value");
    }

    @Test
    public void testExtractList() {
        JSONExtractor jsonExtractor = new JSONExtractor();
        String json = "{\"key1\":[\"value1\",\"value2\"]}";
        String key = "key1";
        JSONArray result = jsonExtractor.extractList(json, key);
        List<String> resultList = new ArrayList<>();
        for (int i = 0; i < result.length(); i++) {
            try
            {
                resultList.add(result.getString(i));
            }
            catch(JSONException e)
            {
                e.printStackTrace();
            }
        }

        assertThat(resultList).containsExactlyInAnyOrder("value1", "value2");
    }

    @Test
    public void testReadFromFile() {
        String fileName = "src/test/resources/testJsonFile.json";
        String[] keys = {"key1", "key2"};
        JSONExtractor jsonExtractor = new JSONExtractor();
        String[] result = jsonExtractor.readFromFile(fileName, keys);

        assertThat(result).containsExactly("value1", "value2");
    }
}

