package edu.kafka.consumer.service;

import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.kstream.*;
import org.apache.kafka.streams.kstream.internals.WindowedDeserializer;
import org.apache.kafka.streams.kstream.internals.WindowedSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Properties;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Service
public class ConsumerServiceImpl implements ConsumerService {

    private String topicName;
    private String topicWindowedName;
    private long windowSize;
    private Properties consumerConfig;
    private Lock lock;
    private KafkaStreams streams;

    @Autowired
    public ConsumerServiceImpl(@Qualifier("topicName") String topicName, @Qualifier("topicWindowedName") String topicWindowedName,
                               @Qualifier("windowSize") long windowSize, @Qualifier("consumerConfig") Properties consumerConfig) {
        this.topicName = topicName;
        this.topicWindowedName = topicWindowedName;
        this.windowSize = windowSize * 1000L; //from seconds to millis
        this.consumerConfig = consumerConfig;
        this.lock = new ReentrantLock();
    }

    public String start() {
        String status = status();
        if (lock.tryLock()) {
            try {
                if (streams == null) {
                    KStreamBuilder builder = new KStreamBuilder();
                    KStream<String, Long> stream = builder.stream(keySerde(), valueSerde(), topicName);
                    KGroupedStream<String, Long> groupedStream = stream.groupByKey();
                    KTable<Windowed<String>, Long> countedStream = groupedStream.count(TimeWindows.of(windowSize), topicName + "-count");
                    countedStream.to(windowedSerde(), valueSerde(), topicWindowedName);
                    countedStream.print();

                    streams = new KafkaStreams(builder, consumerConfig);
                    streams.start();
                    status = "started";
                }
            } finally {
                lock.unlock();
            }
        }
        return status;
    }

    public String stop() {
        if (lock.tryLock()) {
            try {
                if (streams != null) {
                    streams.close();
                    streams = null;
                }
            } finally {
                lock.unlock();
            }
        }
        return status();
    }

    public String status() {
        return (streams != null) ? "started: see console log" : "stopped";
    }

    private Serde<String> keySerde() {
        return Serdes.String();
    }

    private Serde<Long> valueSerde() {
        return Serdes.Long();
    }

    private Serde<Windowed<String>> windowedSerde() {
        return Serdes.serdeFrom(new WindowedSerializer<>(keySerde().serializer()), new WindowedDeserializer<>(keySerde().deserializer()));
    }
}
