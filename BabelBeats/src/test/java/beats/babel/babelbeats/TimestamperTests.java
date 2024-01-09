package beats.babel.babelbeats;
import static org.junit.jupiter.api.Assertions.*;

import org.json.JSONException;
import org.junit.jupiter.api.Test;
import java.io.File;
import org.json.JSONObject;


public class TimestamperTests {
    @Test
    public void testSaveTimestamps() {
        String name = "okudakun_oeoe";
        long size = 0;
        Timestamper timestamper = new Timestamper();
        JSONObject result = timestamper.saveTimestamps(name, size);
        try {
            long resultId = result.getInt("_id");
            String resultTitle = result.getString("title");
            assertEquals(size + 1, resultId);
            assertEquals(name, resultTitle);
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        assertNotNull(result);

        }
}

