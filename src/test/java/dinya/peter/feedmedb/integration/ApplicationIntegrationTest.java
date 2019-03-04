package dinya.peter.feedmedb.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import dinya.peter.feedmedb.Resources;
import dinya.peter.feedmedb.model.Event;
import dinya.peter.feedmedb.model.Market;
import dinya.peter.feedmedb.model.Outcome;
import dinya.peter.feedmedb.repo.FeedMeRepository;
import dinya.peter.feedmedb.resource.EventResource;
import dinya.peter.feedmedb.resource.MarketResource;
import dinya.peter.feedmedb.resource.OutcomeResource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.ContainerTestUtils;
import org.springframework.test.context.ActiveProfiles;

import java.io.IOException;
import java.util.Iterator;
import java.util.Set;

import static java.util.stream.Collectors.toSet;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@EmbeddedKafka(topics = "test-topic", partitions = 1)
@ActiveProfiles("test")
public class ApplicationIntegrationTest {
    private static final int FIVE_SECONDS = 5000;
    @Autowired
        KafkaListenerEndpointRegistry defaultKafkaListenerEndpointRegistry;
    @Autowired
    EmbeddedKafkaBroker embeddedKafka;
    @Autowired
    KafkaTemplate<?, String> kafkaTemplate;
    @Autowired
    ObjectMapper objectMapper;
    @Value("${spring.kafka.topic}")
    String topic;

    @Autowired
    FeedMeRepository repository;

    @BeforeEach
    void setUp() {
        for (var listenerContainer : defaultKafkaListenerEndpointRegistry.getListenerContainers()) {
            if (listenerContainer.isRunning()) {
                ContainerTestUtils.waitForAssignment(listenerContainer, embeddedKafka.getPartitionsPerTopic());
            }
        }
    }

    @Test
    void shouldCreateHierarchy() throws IOException, InterruptedException {
        var eventResource = objectMapper.readValue(Resources.EVENT_CREATE_MESSAGE, EventResource.class);
        var marketResource = objectMapper.readValue(Resources.MARKET_CREATE_MESSAGE, MarketResource.class);
        var outcomeResource = objectMapper.readValue(Resources.OUTCOME_CREATE_MESSAGE, OutcomeResource.class);

        sendKafkaMessages(Resources.EVENT_CREATE_MESSAGE, Resources.MARKET_CREATE_MESSAGE, Resources.OUTCOME_CREATE_MESSAGE);
        // make sure that messages get processed
        Thread.sleep(FIVE_SECONDS);

        assertEvent(repository.findAll().get(0), eventResource, Set.of(marketResource), Set.of(outcomeResource));
    }

    @Test
    void shouldUpdateFields() throws IOException, InterruptedException {
        var eventResource = objectMapper.readValue(Resources.EVENT_UPDATE_MESSAGE, EventResource.class);
        var marketResource = objectMapper.readValue(Resources.MARKET_UPDATE_MESSAGE, MarketResource.class);
        var outcomeResource = objectMapper.readValue(Resources.OUTCOME_UPDATE_MESSAGE, OutcomeResource.class);

        sendKafkaMessages(Resources.EVENT_CREATE_MESSAGE, Resources.MARKET_CREATE_MESSAGE, Resources.OUTCOME_CREATE_MESSAGE);
        sendKafkaMessages(Resources.EVENT_UPDATE_MESSAGE, Resources.MARKET_UPDATE_MESSAGE, Resources.OUTCOME_UPDATE_MESSAGE);
        // make sure that messages get processed
        Thread.sleep(FIVE_SECONDS);
        assertEvent(repository.findAll().get(0), eventResource, Set.of(marketResource), Set.of(outcomeResource));
    }

    private void sendKafkaMessages(String eventCreateMessage, String marketCreateMessage, String outcomeCreateMessage) {
        kafkaTemplate.send(topic, eventCreateMessage);
        kafkaTemplate.send(topic, marketCreateMessage);
        kafkaTemplate.send(topic, outcomeCreateMessage);
    }

    private void assertEvent(Event event, EventResource eventResource, Set<MarketResource> marketResources, Set<OutcomeResource> outcomeResources) {
        assertEquals(event.getId(), eventResource.getEventId());
        assertEquals(event.getCategory(), eventResource.getCategory());
        assertEquals(event.getName(), eventResource.getName());
        assertEquals(event.getSubCategory(), eventResource.getSubCategory());
        assertEquals(event.getStartTime(), eventResource.getStartTime());
        assertMarkets(event.getMarkets(), marketResources, outcomeResources);
    }

    private void assertMarkets(Set<Market> markets, Set<MarketResource> marketResources, Set<OutcomeResource> outcomeResources) {
        assertEquals(markets.size(), marketResources.size());
        Iterator<Market> marketsIteration = markets.iterator();
        Iterator<MarketResource> marketResourcesIteration = marketResources.iterator();
        while (marketsIteration.hasNext()) {
            var market = marketsIteration.next();
            var marketResource = marketResourcesIteration.next();
            assertEquals(market.getId(), marketResource.getMarketId());
            assertEquals(market.getName(), marketResource.getName());
            var marketSpecificOutcomes = outcomeResources.stream().filter(o -> o.getMarketId().equals(market.getId())).collect(toSet());
            assertOutcomes(market.getOutcomes(), marketSpecificOutcomes);
        }
    }

    private void assertOutcomes(Set<Outcome> outcomes, Set<OutcomeResource> outcomeResources) {
        assertEquals(outcomes.size(), outcomeResources.size());
        Iterator<Outcome> outcomesIteration = outcomes.iterator();
        Iterator<OutcomeResource> outcomeResourcesIteration = outcomeResources.iterator();
        while (outcomesIteration.hasNext()) {
            var outcome = outcomesIteration.next();
            var outcomeResource = outcomeResourcesIteration.next();
            assertEquals(outcome.getId(), outcomeResource.getOutcomeId());
            assertEquals(outcome.getName(), outcomeResource.getName());
            assertEquals(outcome.getPrice(), outcomeResource.getPrice());
        }
    }
}
