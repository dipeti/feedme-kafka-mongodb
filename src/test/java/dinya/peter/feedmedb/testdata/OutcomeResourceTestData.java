package dinya.peter.feedmedb.testdata;

import dinya.peter.feedmedb.resource.OutcomeResource;

public class OutcomeResourceTestData {
    public static OutcomeResource anOutcomeResourceWithOperation(String operation) {
        return new OutcomeResource("msgId", operation, "outcome", 1L, "marketId", "outcomeId", "outcomeName",
                "price", true, false);
    }

    public static OutcomeResource anOutcomeResourceWithMarketId(String operation, String marketId) {
        return new OutcomeResource("msgId", operation, "outcome", 1L, marketId, "outcomeId", "outcomeName",
                "price", true, false);
    }
}
