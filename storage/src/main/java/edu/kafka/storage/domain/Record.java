package edu.kafka.storage.domain;

import org.apache.kafka.streams.kstream.Windowed;
import org.springframework.data.cassandra.mapping.*;

import java.io.Serializable;
import java.util.Date;

@Table("aggregation")
public class Record implements Serializable {

    private static final long serialVersionUID = 1L;

    @PrimaryKey
    private RecordPK key;
    @Column("count")
    private long value;

    public Record() {
    }

    public Record(Windowed<String> key, long value, AggregationTime time) {
        this.key = new RecordPK(key.key(), time.getValue(), new Date(key.window().start()));
        this.value = value;
        System.out.printf("%d: %s:%d\n", this.key.getTimestamp().getTime(), this.key.getName(), value);
    }

    public RecordPK getKey() {
        return key;
    }

    public long getValue() {
        return value;
    }
}