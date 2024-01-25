package beats.babel.babelbeats;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface BlackListRepository extends MongoRepository<BlackRecord, String>{
    BlackRecord findByTitle(String title);
    long count();
}
