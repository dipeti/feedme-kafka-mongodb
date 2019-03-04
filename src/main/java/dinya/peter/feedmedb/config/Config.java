package dinya.peter.feedmedb.config;

import dinya.peter.feedmedb.resource.DomainResource;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.listener.SeekToCurrentErrorHandler;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableKafka
@EnableMongoRepositories(basePackages = "dinya.peter.feedmedb.repo")
public class Config {
    @Bean
    KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, DomainResource>>
    kafkaListenerContainerFactory(ConsumerFactory<String, DomainResource> consumerFactory) {
        ConcurrentKafkaListenerContainerFactory<String, DomainResource> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory);
        factory.setStatefulRetry(true);
        factory.setErrorHandler(new SeekToCurrentErrorHandler());
        return factory;
    }

    @Bean
    public ConsumerFactory<String, DomainResource> consumerFactory(KafkaProperties kafkaProperties) {
        Map<String, Object> config = new HashMap<>(kafkaProperties.buildConsumerProperties());
        config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaProperties.getBootstrapServers());
        return new DefaultKafkaConsumerFactory<>(config, new StringDeserializer(), new JsonDeserializer<>(DomainResource.class, false));
    }

    @Bean
    public Map<String, Object> consumerConfigs(KafkaProperties kafkaProperties) {
        return Map.of(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaProperties.getBootstrapServers());
    }
}
