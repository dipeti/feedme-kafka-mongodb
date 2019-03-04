package dinya.peter.feedmedb.repo;

import dinya.peter.feedmedb.model.Event;
import dinya.peter.feedmedb.resource.EventResource;
import dinya.peter.feedmedb.resource.MarketResource;
import dinya.peter.feedmedb.resource.OutcomeResource;

public interface CustomRepository {
    Event saveEvent(EventResource eventResource);

    Event addMarket(MarketResource marketResource);

    Event addOutcome(OutcomeResource outcomeResource);

    Event updateEvent(EventResource domainResource);

    long updateMarket(MarketResource marketResource);

    long updateOutcome(OutcomeResource outcomeResource);
}
