package dinya.peter.feedmedb.resource;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;

import java.util.Optional;

@ToString(callSuper = true)
@Getter
public class MarketResource extends DomainResource {
    private final String marketId;
    private final String eventId;
    private final String name;
    private final boolean displayed;
    private final boolean suspended;

    public MarketResource(@JsonProperty("msgId") String msgId, @JsonProperty("operation") String operation, @JsonProperty("type") String type,
                          @JsonProperty("timestamp") Long timestamp, @JsonProperty("marketId") String marketId, @JsonProperty("eventId") String eventId,
                          @JsonProperty("name") String name, @JsonProperty("displayed") boolean displayed, @JsonProperty("suspended") boolean suspended) {
        super(msgId, operation, type, timestamp);
        this.marketId = marketId;
        this.eventId = eventId;
        this.name = name;
        this.displayed = displayed;
        this.suspended = suspended;
    }

    @Override
    public Optional<String> getParentId() {
        return Optional.of(eventId);
    }
}
