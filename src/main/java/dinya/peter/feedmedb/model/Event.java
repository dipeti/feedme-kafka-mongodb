package dinya.peter.feedmedb.model;

import dinya.peter.feedmedb.resource.EventResource;
import lombok.Value;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Collections;
import java.util.Set;

@Document
@Value
public class Event {
    private final String id;
    private final String category;
    private final String subCategory;
    private final String name;
    private final long startTime;
    private final boolean displayed;
    private final boolean suspended;
    private Set<Market> markets;

    public static Event of(EventResource eventResource) {
        return new Event(eventResource.getEventId(), eventResource.getCategory(), eventResource.getSubCategory(),
                eventResource.getName(), eventResource.getStartTime(), eventResource.isDisplayed(),
                eventResource.isSuspended(), Collections.emptySet());
    }
}
