package dinya.peter.feedmedb.testdata;

import dinya.peter.feedmedb.resource.EventResource;

public class EventResourceTestData {
    public static EventResource anEventResourceWithOperation(String operation) {
        return new EventResource("msgId", operation, "event", 1L, "eventId", "category", "subcategory",
                "eventName", 2L, true, false);
    }
}
