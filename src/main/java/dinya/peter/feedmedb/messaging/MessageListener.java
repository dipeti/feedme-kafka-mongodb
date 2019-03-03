package dinya.peter.feedmedb.messaging;

import dinya.peter.feedmedb.model.Domain;
import dinya.peter.feedmedb.repo.FeedMeRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Log4j2
public class MessageListener {
    private final MongoTemplate mongoTemplate;
    private final FeedMeRepository repo;

    public MessageListener(FeedMeRepository repo, MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
        this.repo = repo;
    }

    @KafkaListener(topics = "${spring.kafka.topic}", groupId = "mongo-clients", concurrency = "${spring.kafka.listener.concurrency:4}")
    public void listen(Domain domain) {
        log.info(domain.getMsgId());
        switch (domain.getOperation()) {
            case "create":
                break;
            case "update":
                break;
            default:
                log.warn("Ignoring message=[{}]", domain);
        }
    }
}
