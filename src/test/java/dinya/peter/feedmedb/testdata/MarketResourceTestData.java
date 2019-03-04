package dinya.peter.feedmedb.testdata;

import dinya.peter.feedmedb.resource.MarketResource;

public class MarketResourceTestData {
    public static MarketResource aMarketResourceWithOperation(String operation) {
        return new MarketResource("msgId", operation, "market", 1L, "marketId", "eventId", "marketName",
                true, false);
    }
}
