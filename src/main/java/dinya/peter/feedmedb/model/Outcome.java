package dinya.peter.feedmedb.model;

import dinya.peter.feedmedb.resource.OutcomeResource;
import lombok.Value;

@Value
public class Outcome {
    private final String id;
    private final String name;
    private final String price;
    private final boolean displayed;
    private final boolean suspended;

    public static Outcome of(OutcomeResource outcomeResource) {
        return new Outcome(outcomeResource.getOutcomeId(), outcomeResource.getName(),
                outcomeResource.getPrice(), outcomeResource.getDisplayed(), outcomeResource.getSuspended());
    }
}
