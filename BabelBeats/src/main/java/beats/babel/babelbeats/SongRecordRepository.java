package beats.babel.babelbeats;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SongRecordRepository extends MongoRepository<SongRecord, String>{
}
