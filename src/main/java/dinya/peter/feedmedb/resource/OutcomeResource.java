package dinya.peter.feedmedb.resource;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;

import java.util.Optional;

@ToString(callSuper = true)
@Getter
public class OutcomeResource extends DomainResource {
    private final String marketId;
    private final String outcomeId;
    private final String name;
    private final String price;
    private final Boolean displayed;
    private final Boolean suspended;

    @JsonCreator
    public OutcomeResource(@JsonProperty("msgId") String msgId, @JsonProperty("operation") String operation, @JsonProperty("type") String type,
                           @JsonProperty("timestamp") Long timestamp, @JsonProperty("marketId") String marketId, @JsonProperty("outcomeId") String outcomeId,
                           @JsonProperty("name") String name, @JsonProperty("price") String price, @JsonProperty("displayed") Boolean displayed,
                           @JsonProperty("suspended") Boolean suspended) {
        super(msgId, operation, type, timestamp);
        this.marketId = marketId;
        this.outcomeId = outcomeId;
        this.name = name;
        this.price = price;
        this.displayed = displayed;
        this.suspended = suspended;
    }

    @Override
    public Optional<String> getParentId() {
        return Optional.of(marketId);
    }

}
