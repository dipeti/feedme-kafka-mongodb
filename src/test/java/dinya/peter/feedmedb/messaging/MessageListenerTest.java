package dinya.peter.feedmedb.messaging;

import dinya.peter.feedmedb.repo.FeedMeRepository;
import dinya.peter.feedmedb.resource.EventResource;
import dinya.peter.feedmedb.resource.MarketResource;
import dinya.peter.feedmedb.resource.OutcomeResource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static dinya.peter.feedmedb.testdata.EventResourceTestData.anEventResourceWithOperation;
import static dinya.peter.feedmedb.testdata.MarketResourceTestData.aMarketResourceWithOperation;
import static dinya.peter.feedmedb.testdata.OutcomeResourceTestData.anOutcomeResourceWithMarketId;
import static dinya.peter.feedmedb.testdata.OutcomeResourceTestData.anOutcomeResourceWithOperation;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MessageListenerTest {

    @Mock
    private FeedMeRepository mockRepo;
    private MessageListener unit;

    @BeforeEach
    void setUp() {
        unit = new MessageListener(mockRepo);
    }

    @Test
    void callsSaveEvent() {
        EventResource event = anEventResourceWithOperation("create");

        unit.listen(event);

        verify(mockRepo).saveEvent(eq(event));
    }

    @Test
    void callsAddMarket() {
        MarketResource market = aMarketResourceWithOperation("create");

        unit.listen(market);

        verify(mockRepo).addMarket(eq(market));
    }

    @Test
    void callsAddOutcome() {
        OutcomeResource outcome = anOutcomeResourceWithOperation("create");

        unit.listen(outcome);

        verify(mockRepo).addOutcome(eq(outcome));
    }

    @Test
    void callsUpdateEvent() {
        EventResource event = anEventResourceWithOperation("update");

        unit.listen(event);

        verify(mockRepo).updateEvent(eq(event));
    }

    @Test
    void callsUpdateMarket() {
        MarketResource market = aMarketResourceWithOperation("update");

        unit.listen(market);

        verify(mockRepo).updateMarket(eq(market));
    }

    @Test
    void callsUpdateOutcome() {
        OutcomeResource outcome = anOutcomeResourceWithOperation("update");

        unit.listen(outcome);

        verify(mockRepo).updateOutcome(eq(outcome));
    }

    @Test
    void shouldNotCallUpdateOutcomeIfParentIdIsNull() {
        OutcomeResource outcome = anOutcomeResourceWithMarketId("update", null);

        unit.listen(outcome);

        verify(mockRepo, never()).addOutcome(any());
    }
}