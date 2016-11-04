package edu.kafka.producer.service;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Service
public class ProducerServiceImpl implements ProducerService {

    private RecordGenerator recordGenerator;
    private String topicName;
    private Properties producerConfig;
    private ExecutorService executor;
    private Future<?> future;
    private Lock lock;

    @Autowired
    public ProducerServiceImpl(RecordGenerator recordGenerator, @Qualifier("topicName") String topicName,
                               @Qualifier("producerConfig") Properties producerConfig) {
        this.recordGenerator = recordGenerator;
        this.topicName = topicName;
        this.producerConfig = producerConfig;
        this.executor = Executors.newSingleThreadExecutor();
        this.future = CompletableFuture.completedFuture("initial value");
        this.lock = new ReentrantLock();
    }

    public String start() {
        String status = status();
        if (lock.tryLock()) {
            try {
                if (future.isDone()) {
                    future = executor.submit(new ProducerTask());
                    status = "started";
                }
            } finally {
                lock.unlock();
            }
        }
        return status;
    }

    public String status() {
        return future.isDone() ? "done" : "in progress -> " + recordGenerator.status();
    }

    private class ProducerTask implements Runnable {

        @Override
        public void run() {
            recordGenerator.reset();
            List<Future<RecordMetadata>> futures = new ArrayList<>((int) recordGenerator.size());
            try (KafkaProducer<String, Long> producer = new KafkaProducer<>(producerConfig)) {
                while (recordGenerator.hasNext()) {
                    Map.Entry<String, Long> next = recordGenerator.next();
                    Future<RecordMetadata> meta = producer.send(new ProducerRecord<>(topicName, next.getKey(), next.getValue()));
                    futures.add(meta);
                }
            }
            Set<Integer> set = new HashSet<>();
            futures.forEach(meta -> {
                if (meta.isDone()) {
                    try {
                        set.add(meta.get().partition());
                    } catch (Exception ignored) {
                    }
                }
            });
            System.out.println("Used partitions: " + Arrays.toString(set.toArray()));
        }
    }
}
