package dinya.peter.feedmedb.model;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Getter;
import lombok.ToString;

import java.util.Optional;


@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "type")
@JsonSubTypes(value = {
        @JsonSubTypes.Type(value = Event.class, name = "event"),
        @JsonSubTypes.Type(value = Market.class, name = "market"),
        @JsonSubTypes.Type(value = Outcome.class, name = "outcome"),
})
@Getter
@ToString
public abstract class Domain {
    private final String msgId;
    private final String operation;
    private final String type;
    private final Long timestamp;

    public Domain(String msgId, String operation, String type, Long timestamp) {
        this.msgId = msgId;
        this.operation = operation;
        this.type = type;
        this.timestamp = timestamp;
    }

    public abstract Optional<String> getParentId();
}
