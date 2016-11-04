package edu.kafka.producer.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.AbstractMap;
import java.util.Map;
import java.util.Random;

@Component
public class RecordGenerator {

    private Random random;
    private String[] keys;
    private long records;
    private long timeout;
    private long counter;

    @Autowired
    public RecordGenerator(@Qualifier("generatorKeys") String[] keys, @Qualifier("generatorRecords") long records,
                           @Qualifier("generatorTimeout") long timeout) {
        this.random = new Random(System.currentTimeMillis());
        this.keys = keys;
        this.records = records;
        this.timeout = timeout;
        this.counter = 0;
    }

    public void reset() {
        this.counter = 0;
    }

    public boolean hasNext() {
        boolean hasNext = counter < records;
        if (counter == 0) {
            beforeFirst();
        } else if (!hasNext) {
            afterLast();
        }
        try {
            Thread.sleep(timeout);
        } catch (InterruptedException ignored) {}

        return hasNext;
    }

    public Map.Entry<String, Long> next() {
        //one random key
        String key = keys[random.nextInt(keys.length)];
        //long between 0 and 10
        long value = Math.abs(random.nextLong() % 10);
        counter++;
        System.out.printf("\r%d:%d -> %s:%d", records, counter, key, value);
        return new AbstractMap.SimpleEntry<>(key, value);
    }

    public String status() {
        return String.format("%d:%d", records, counter);
    }

    public long size() {
        return records;
    }

    public void beforeFirst() {
        System.out.println();
    }

    public void afterLast() {
        System.out.println();
    }
}
