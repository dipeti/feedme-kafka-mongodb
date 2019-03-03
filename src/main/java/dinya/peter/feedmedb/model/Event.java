package dinya.peter.feedmedb.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.ToString;

import java.util.Optional;

@ToString(callSuper = true)
public class Event extends Domain {
    private final String eventId;
    private final String category;
    private final String subCategory;
    private final String name;
    private final long startTime;
    private final boolean displayed;
    private final boolean suspended;

    public Event(@JsonProperty("msgId") String msgId, @JsonProperty("operation") String operation, @JsonProperty("type") String type, @JsonProperty("timestamp") Long timestamp, @JsonProperty("eventId") String eventId, @JsonProperty("category") String category, @JsonProperty("subCategory") String subCategory, @JsonProperty("name") String name, @JsonProperty("startTime") long startTime, @JsonProperty("displayed") boolean displayed, @JsonProperty("suspended") boolean suspended) {
        super(msgId, operation, type, timestamp);
        this.eventId = eventId;
        this.category = category;
        this.subCategory = subCategory;
        this.name = name;
        this.startTime = startTime;
        this.displayed = displayed;
        this.suspended = suspended;
    }

    @Override
    public Optional<String> getParentId() {
        return Optional.empty();
    }
}
