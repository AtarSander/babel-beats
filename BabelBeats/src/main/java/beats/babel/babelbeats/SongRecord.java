package beats.babel.babelbeats;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.List;

@Document(collection = "savedSongs")
public class SongRecord {
    @Id
    private String _id;

    private String title;
    private List<Timestamp> timestamps;

    public static class Timestamp {
        private double value;
        private String key;

    }
}