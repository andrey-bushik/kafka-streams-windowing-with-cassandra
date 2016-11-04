package edu.kafka.consumer;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import java.util.Properties;

@Configuration
public class ConsumerConfiguration {

    @Bean
    public String topicName(Environment environment) {
        return environment.getProperty("consumer.topic.name");
    }

    @Bean
    public String topicWindowedName(Environment environment) {
        return environment.getProperty("consumer.topic.windowed.name");
    }

    @Bean
    public long windowSize(Environment environment) {
        String value = environment.getProperty("consumer.window.size");
        return Integer.valueOf(value);
    }

    @Bean
    public Properties consumerConfig(Environment environment) {
        Properties properties = new Properties();
        properties.put(StreamsConfig.APPLICATION_ID_CONFIG, environment.getProperty("kafka.application.id"));
        properties.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, environment.getProperty("kafka.bootstrap.servers"));
        properties.put(StreamsConfig.KEY_SERDE_CLASS_CONFIG, Serdes.StringSerde.class);
        properties.put(StreamsConfig.VALUE_SERDE_CLASS_CONFIG, Serdes.LongSerde.class);
        return properties;
    }
}