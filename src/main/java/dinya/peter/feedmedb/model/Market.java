package dinya.peter.feedmedb.model;

import dinya.peter.feedmedb.resource.MarketResource;
import lombok.Value;

import java.util.Collections;
import java.util.Set;

@Value
public class Market {
    private final String id;
    private final String name;
    private final boolean displayed;
    private final boolean suspended;
    private final Set<Outcome> outcomes;

    public static Market of(MarketResource marketResource) {
        return new Market(marketResource.getMarketId(), marketResource.getName(), marketResource.isDisplayed(), marketResource.isSuspended(), Collections.emptySet());
    }
}

