package dinya.peter.feedmedb.repo;

import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.result.UpdateResult;
import dinya.peter.feedmedb.model.Event;
import dinya.peter.feedmedb.model.Market;
import dinya.peter.feedmedb.model.Outcome;
import dinya.peter.feedmedb.resource.DomainResource;
import dinya.peter.feedmedb.resource.EventResource;
import dinya.peter.feedmedb.resource.MarketResource;
import dinya.peter.feedmedb.resource.OutcomeResource;
import lombok.extern.log4j.Log4j2;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.mongodb.client.model.Filters.eq;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

@Component
@Log4j2
public class CustomRepositoryImpl implements CustomRepository {
    private static final String COLLECTION = Event.class.getSimpleName().toLowerCase();
    private static final String MARKETS = "markets";
    private static final String OUTCOMES = "outcomes";
    private static final String NESTED_OUTCOMES = "markets.$.outcomes";
    private static final String ELEMENT_NAME = ".$[element].name";
    private static final String ELEMENT_DISPLAYED = ".$[element].displayed";
    private static final String ELEMENT_SUSPENDED = ".$[element].suspended";
    private static final String ELEMENT_ID = "element._id";
    private static final String ELEMENT_PRICE = ".$[element].price";

    private final MongoTemplate mongoTemplate;

    public CustomRepositoryImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public Event saveEvent(EventResource eventResource) {
        return mongoTemplate.save(Event.of(eventResource));
    }

    @Override
    public Event addMarket(MarketResource marketResource) {
        return updateEvent(marketResource.getParentId().get(), new Update().addToSet(MARKETS, Market.of(marketResource)));
    }

    @Override
    public Event addOutcome(OutcomeResource outcomeResource) {
        return updateMarket(outcomeResource.getParentId().get(), new Update().addToSet(NESTED_OUTCOMES, Outcome.of(outcomeResource)));
    }

    @Override
    public Event updateEvent(EventResource eventResource) {
        Update update = new Update()
                .set("startTime", eventResource.getStartTime())
                .set("displayed", eventResource.isDisplayed())
                .set("suspended", eventResource.isSuspended())
                .set("category", eventResource.getCategory())
                .set("subCategory", eventResource.getSubCategory());
        return updateEvent(eventResource.getEventId(), update);
    }

    @Override
    public long updateMarket(MarketResource marketResource) {
        UpdateResult result = buildUpdateOne(MARKETS + "._id", marketResource.getMarketId(),
                new Document().append(MARKETS + ELEMENT_NAME, marketResource.getName())
                        .append(MARKETS + ELEMENT_DISPLAYED, marketResource.isDisplayed())
                        .append(MARKETS + ELEMENT_SUSPENDED, marketResource.isSuspended()));
        logAndThrowIfUnaffected(marketResource, result);
        return result.getModifiedCount();
    }

    @Override
    public long updateOutcome(OutcomeResource outcomeResource) {
        UpdateResult result = buildUpdateOne(MARKETS + "." + OUTCOMES + "._id", outcomeResource.getOutcomeId(),
                new Document().append(NESTED_OUTCOMES + ELEMENT_NAME, outcomeResource.getName())
                        .append(NESTED_OUTCOMES + ELEMENT_DISPLAYED, outcomeResource.getDisplayed())
                        .append(NESTED_OUTCOMES + ELEMENT_SUSPENDED, outcomeResource.getSuspended())
                        .append(NESTED_OUTCOMES + ELEMENT_PRICE, outcomeResource.getPrice()));
        logAndThrowIfUnaffected(outcomeResource, result);
        return result.getModifiedCount();
    }

    private UpdateResult buildUpdateOne(String pathToId, String id, Bson update) {
        return mongoTemplate.getCollection(COLLECTION)
                .updateOne(eq(pathToId, id), new Document().append("$set", update),
                        new UpdateOptions().arrayFilters(List.of(eq(ELEMENT_ID, id))));
    }

    private void logAndThrowIfUnaffected(DomainResource resource, UpdateResult result) {
        if (result.getModifiedCount() < 1 && result.getMatchedCount() < 1) {
            log.warn("Update did not make any changes, [{}]", resource);
            throw new RuntimeException(String.format("Update did not make any changes with msgId=[%s]!", resource.getMsgId()));
        }
    }

    private Event updateEvent(String eventId, Update update) {
        return mongoTemplate.findAndModify(query(where("_id").is(eventId)), update, Event.class);
    }

    private Event updateMarket(String marketId, Update update) {
        return mongoTemplate.findAndModify(query(where(MARKETS + "._id").is(marketId)), update, Event.class);
    }
}
