package beats.babel.babelbeats;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;


public class SongRecordTests {


    @Test
    public void testSongRecordProperties() {
        JSONObject songData = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        List<Pair> lyrics = new ArrayList<>();
        lyrics.add(new Pair("Verse 1", 10.5));
        lyrics.add(new Pair("Verse 2", 13.5));

        try {
            for (Pair pair : lyrics) {
                JSONObject pairObject = new JSONObject();
                pairObject.put("key", pair.getKey());
                pairObject.put("value", pair.getValue());
                jsonArray.put(pairObject);
                songData.put("_id", 123L);
                songData.put("title", "Example Song");
                songData.put("timestamps", jsonArray);
            }
            }
            catch (JSONException e) {
                e.printStackTrace();
            }

            SongRecord songRecord = new SongRecord();
            songRecord.loadFromJSON(songData);

            assertThat(songRecord.get_id()).isEqualTo(123L);
            assertThat(songRecord.getTitle()).isEqualTo("Example Song");
            List<SongRecord.Timestamp> timestamps = songRecord.getTimestamps();
            assertThat(timestamps).hasSize(2);
            assertThat(timestamps.get(0).getValue()).isEqualTo(10.5);
            assertThat(timestamps.get(0).getKey()).isEqualTo("Verse 1");

    }

    @Test
    public void testSettersAndGetters() {
        SongRecord songRecord = new SongRecord();
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
