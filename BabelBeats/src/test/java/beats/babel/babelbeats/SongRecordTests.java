package beats.babel.babelbeats;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;


public class SongRecordTests {
    @Test
    public void testSongRecordProperties() {
        JSONObject songData = new JSONObject();
        JSONArray timestampsArray = new JSONArray();
        JSONObject timestampJson = new JSONObject();
        SongRecord songRecord = new SongRecord(songData);
        try{
            songData.put("_id", 123);
            songData.put("title", "Example Song");
            timestampJson.put("value", 10.5);
            timestampJson.put("key", "Verse 1");
            timestampsArray.put(timestampJson);
            songData.put("timestamps", timestampsArray);

        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

        assertThat(songRecord.get_id()).isEqualTo(123);
        assertThat(songRecord.getTitle()).isEqualTo("Example Song");
        List<SongRecord.Timestamp> timestamps = songRecord.getTimestamps();
        assertThat(timestamps).hasSize(1);
        assertThat(timestamps.get(0).getValue()).isEqualTo(10.5);
        assertThat(timestamps.get(0).getKey()).isEqualTo("Verse 1");
    }

    @Test
    public void testSettersAndGetters() {
        SongRecord songRecord = new SongRecord(new JSONObject());
        songRecord.set_id(456);
        songRecord.setTitle("New Song");
        SongRecord.Timestamp timestamp = new SongRecord.Timestamp();
        timestamp.setValue(15.75);
        timestamp.setKey("Chorus");
        songRecord.setTimestamps(List.of(timestamp));

        assertThat(songRecord.get_id()).isEqualTo(456);
        assertThat(songRecord.getTitle()).isEqualTo("New Song");
        List<SongRecord.Timestamp> timestamps = songRecord.getTimestamps();
        assertThat(timestamps).hasSize(1);
        assertThat(timestamps.get(0).getValue()).isEqualTo(15.75);
        assertThat(timestamps.get(0).getKey()).isEqualTo("Chorus");
    }
}
