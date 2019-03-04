package dinya.peter.feedmedb.repo;

import dinya.peter.feedmedb.model.Event;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FeedMeRepository extends MongoRepository<Event, String>, CustomRepository {
}
