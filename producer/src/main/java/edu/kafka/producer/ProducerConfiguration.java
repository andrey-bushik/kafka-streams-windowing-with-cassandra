package edu.kafka.producer;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.util.StringUtils;

import java.util.Properties;

@Configuration
public class ProducerConfiguration {

    @Bean
    public String topicName(Environment environment) {
        return environment.getProperty("producer.topic.name");
    }

    @Bean
    public String[] generatorKeys(Environment environment) {
        String value = environment.getProperty("producer.generator.keys");
        return StringUtils.tokenizeToStringArray(value, ",");
    }

    @Bean
    public long generatorRecords(Environment environment) {
        String value = environment.getProperty("producer.generator.records");
        return Integer.valueOf(value);
    }

    @Bean
    public long generatorTimeout(Environment environment) {
        String value = environment.getProperty("producer.generator.timeout");
        return Integer.valueOf(value);
    }

    @Bean
    public Properties producerConfig(Environment environment) {
        Properties properties = new Properties();
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, environment.getProperty("kafka.bootstrap.servers"));
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, serializerName(Serdes.String()));
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, serializerName(Serdes.Long()));
        return properties;
    }

    private String serializerName(Serde serde) {
        return serde.serializer().getClass().getName();
    }
}