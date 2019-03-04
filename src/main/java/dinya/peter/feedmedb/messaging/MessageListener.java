package dinya.peter.feedmedb.messaging;

import dinya.peter.feedmedb.repo.FeedMeRepository;
import dinya.peter.feedmedb.resource.DomainResource;
import dinya.peter.feedmedb.resource.EventResource;
import dinya.peter.feedmedb.resource.MarketResource;
import dinya.peter.feedmedb.resource.OutcomeResource;
import lombok.extern.log4j.Log4j2;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

@Component
@Log4j2
public class MessageListener {
    private final FeedMeRepository repo;

    public MessageListener(FeedMeRepository repo) {
        this.repo = repo;
    }

    @KafkaListener(topics = "${spring.kafka.topic}", groupId = "mongo-clients", concurrency = "${spring.kafka.listener.concurrency:2}")
    public void listen(DomainResource domainResource) {
        long start = System.nanoTime();
        log.debug(domainResource.getMsgId());
        switch (domainResource.getOperation()) {
            case "create":
                applyOnDocument(domainResource, repo::saveEvent, repo::addMarket, repo::addOutcome);
                break;
            case "update":
                applyOnDocument(domainResource, repo::updateEvent, repo::updateMarket, repo::updateOutcome);
                break;
            default:
                log.warn("Ignoring message=[{}]", domainResource);
        }
        long stop = System.nanoTime();
        log.debug("Processing message took=[{}ms]", TimeUnit.NANOSECONDS.toMillis(stop - start));
    }


    private void applyOnDocument(DomainResource domainResource, Consumer<EventResource> eventConsumer, Consumer<MarketResource> marketConsumer, Consumer<OutcomeResource> outcomeConsumer) {
        if (isEvent(domainResource)) {
            eventConsumer.accept((EventResource) domainResource);
        } else if (isMarket(domainResource)) {
            marketConsumer.accept((MarketResource) domainResource);
        } else if (isOutcome(domainResource)) {
            outcomeConsumer.accept((OutcomeResource) domainResource);
        }
    }

    private boolean isEvent(DomainResource domainResource) {
        return domainResource instanceof EventResource;
    }

    private boolean isOutcome(DomainResource domainResource) {
        return domainResource instanceof OutcomeResource && domainResource.getParentId().isPresent();
    }

    private boolean isMarket(DomainResource domainResource) {
        return domainResource instanceof MarketResource && domainResource.getParentId().isPresent();
    }
}
