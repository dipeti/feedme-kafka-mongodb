package dinya.peter.feedmedb.resource;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Getter;
import lombok.ToString;

import java.util.Optional;


@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "type")
@JsonSubTypes(value = {
        @JsonSubTypes.Type(value = EventResource.class, name = "event"),
        @JsonSubTypes.Type(value = MarketResource.class, name = "market"),
        @JsonSubTypes.Type(value = OutcomeResource.class, name = "outcome"),
})
@Getter
@ToString
public abstract class DomainResource {
    private final String msgId;
    private final String operation;
    private final String type;
    private final Long timestamp;

    public DomainResource(String msgId, String operation, String type, Long timestamp) {
        this.msgId = msgId;
        this.operation = operation;
        this.type = type;
        this.timestamp = timestamp;
    }

    public abstract Optional<String> getParentId();
}
