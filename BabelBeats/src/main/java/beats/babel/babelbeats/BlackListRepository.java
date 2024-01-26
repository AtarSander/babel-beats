package beats.babel.babelbeats;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.mongodb.repository.Query;


@Repository
public interface BlackListRepository extends MongoRepository<BlackRecord, String>{
    BlackRecord findByTitle(String title);
    long count();
    @Query(value = "{}", sort = "{_id : -1}")
    BlackRecord findDocumentWithHighestId();
}
