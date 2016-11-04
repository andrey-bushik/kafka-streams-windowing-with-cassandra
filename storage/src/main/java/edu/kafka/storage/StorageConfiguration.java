package edu.kafka.storage;

import edu.kafka.storage.domain.AggregationTime;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.cassandra.repository.config.EnableCassandraRepositories;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.Properties;

@Configuration
@EnableCassandraRepositories
public class StorageConfiguration extends WebMvcConfigurerAdapter {

    @Override
    public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
        super.configureContentNegotiation(configurer);
        configurer.defaultContentType(MediaType.APPLICATION_JSON);
    }

    @Bean
    public String topicName(Environment environment) {
        return environment.getProperty("consumer.topic.name");
    }

    @Bean
    public Properties consumerConfig(Environment environment) {
        Properties properties = new Properties();
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, environment.getProperty("kafka.group.id"));
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, environment.getProperty("kafka.bootstrap.servers"));
        properties.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
        properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        return properties;
    }

    @Bean
    public AggregationTime aggregationTime() {
        return AggregationTime.MIN5;
    }
}