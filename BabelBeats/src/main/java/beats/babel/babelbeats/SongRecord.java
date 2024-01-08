package beats.babel.babelbeats;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.List;
import org.json.JSONObject;
import org.json.JSONArray;
import java.util.ArrayList;

@Document(collection = "savedSongs")
public class SongRecord {
    @Id
    private int _id;
    private String title;
    private List<Timestamp> timestamps = new ArrayList<>();


    public static class Timestamp {
        private double value;
        private String key;
        public double getValue() {
            return value;
        }

        public void setValue(double value) {
            this.value = value;
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

    }
    public SongRecord(JSONObject songData)
    {
        this.title = (String) songData.get("title");
        this._id = (int) songData.get("_id");
        JSONArray timestampsArray = (JSONArray) songData.get("timestamps");
        for (Object obj : timestampsArray) {
            JSONObject timestampJson = (JSONObject) obj;
            Timestamp timestamp = new Timestamp();
            timestamp.setValue((Double) timestampJson.get("value"));
            timestamp.setKey((String) timestampJson.get("key"));
            this.timestamps.add(timestamp);
        }
    }
    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Timestamp> getTimestamps() {
        return timestamps;
    }

    public void setTimestamps(List<Timestamp> timestamps) {
        this.timestamps = timestamps;
    }
}