package beats.babel.babelbeats;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SongRecordRepository extends MongoRepository<SongRecord, String>{
    SongRecord findByTitle(String title);
    long count();
}
