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
    private long _id;
    private String title;
    private List<Timestamp> timestamps = new ArrayList<>();
    private List<Timestamp> timestampsTranslated = new ArrayList<>();


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

    public void loadFromJSON(JSONObject songData) {
        this.title = (String) songData.get("title");
        this._id = (long) songData.get("_id");
        JSONArray timestampsArray = (JSONArray) songData.get("timestamps");
        JSONArray timestampsTranslated = (JSONArray) songData.get("timestampsTranslated");
        for (int i = 0; i < timestampsArray.length(); i++) {
            JSONObject timestampJson = timestampsArray .getJSONObject(i);
            JSONObject translatedJSON = timestampsTranslated.getJSONObject(i);
            Timestamp timestamp = new Timestamp();
            timestamp.setValue((Double) timestampJson.get("value"));
            timestamp.setKey((String) timestampJson.get("key"));
            Timestamp timestampTranslated = new Timestamp();
            timestampTranslated.setValue((Double) translatedJSON.get("value"));
            timestampTranslated.setKey((String) translatedJSON.get("key"));

            this.timestamps.add(timestamp);
            this.timestampsTranslated.add(timestampTranslated);
        }
    }

    public long get_id() {
        return _id;
    }

    public void set_id(long _id) {
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

    public List<Timestamp> getTimestampsTranslated() {
        return timestampsTranslated;
    }

    public void setTimestampsTranslated(List<Timestamp> timestamps) {
        this.timestampsTranslated = timestamps;
    }

}