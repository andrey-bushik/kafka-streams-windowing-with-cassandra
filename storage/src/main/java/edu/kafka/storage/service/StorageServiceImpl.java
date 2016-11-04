package edu.kafka.storage.service;

import edu.kafka.storage.domain.AggregationTime;
import edu.kafka.storage.domain.Record;
import edu.kafka.storage.repository.RecordRepository;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.LongDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.streams.kstream.Windowed;
import org.apache.kafka.streams.kstream.internals.WindowedDeserializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class StorageServiceImpl implements StorageService {

    private String topicName;
    private Properties consumerConfig;
    private AggregationTime aggregationTime;
    private RecordRepository recordRepository;
    private long poolTimeout;

    @Autowired
    public StorageServiceImpl(@Qualifier("topicName") String topicName, @Qualifier("consumerConfig") Properties consumerConfig,
                              AggregationTime aggregationTime, RecordRepository recordRepository) {
        this.topicName = topicName;
        this.consumerConfig = consumerConfig;
        this.aggregationTime = aggregationTime;
        this.recordRepository = recordRepository;
        this.poolTimeout = 1500;
    }

    public List<Record> store() {
        List<Record> records = new ArrayList<>();
        try (KafkaConsumer<Windowed<String>, Long> consumer = new KafkaConsumer<>(consumerConfig, keyDeserializer(), longDeserializer())) {
            consumer.subscribe(Collections.singletonList(topicName));

            ConsumerRecords<Windowed<String>, Long> data = consumer.poll(poolTimeout);
            System.out.printf("\nRecords: %d\n", data.count());
            for (ConsumerRecord<Windowed<String>, Long> record : data) {
                records.add(new Record(record.key(), record.value(), aggregationTime));
            }
            recordRepository.save(records);
            consumer.commitSync();
        }
        return records;
    }

    public List<Record> get() {
        return StreamSupport.stream(recordRepository.findAll().spliterator(), false).collect(Collectors.toList());
    }

    private WindowedDeserializer<String> keyDeserializer() {
        return new WindowedDeserializer<>(new StringDeserializer());
    }

    private LongDeserializer longDeserializer() {
        return new LongDeserializer();
    }
}
