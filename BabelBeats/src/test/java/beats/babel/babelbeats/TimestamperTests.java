package beats.babel.babelbeats;
import static org.junit.jupiter.api.Assertions.*;
import org.json.JSONException;
import org.junit.jupiter.api.Test;
import org.json.JSONObject;


public class TimestamperTests {
    @Test
    public void testSaveTimestamps() {
        String name = "Juice_WRLD_Wishing_Well";
        long size = 0;
        Timestamper timestamper = new Timestamper();
        JSONObject result = new JSONObject();
        try {
            result = timestamper.saveTimestamps(name, size, "en", "pl");
            long resultId = result.getInt("_id");
            String resultTitle = result.getString("title");
            assertEquals(size + 1, resultId);
            assertEquals(name, resultTitle);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        assertNotNull(result);
    }
}

