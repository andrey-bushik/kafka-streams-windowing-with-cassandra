package edu.kafka.storage.domain;

import org.springframework.cassandra.core.PrimaryKeyType;
import org.springframework.data.cassandra.mapping.PrimaryKeyClass;
import org.springframework.data.cassandra.mapping.PrimaryKeyColumn;

import java.io.Serializable;
import java.util.Date;

@PrimaryKeyClass
public class RecordPK implements Serializable {

    private static final long serialVersionUID = 1L;

    @PrimaryKeyColumn(name = "event_name", ordinal = 0, type = PrimaryKeyType.PARTITIONED)
    private String name;
    @PrimaryKeyColumn(name = "aggregation_type", ordinal = 1, type = PrimaryKeyType.PARTITIONED)
    private String period;
    @PrimaryKeyColumn(name = "start_time", ordinal = 2, type = PrimaryKeyType.CLUSTERED)
    private Date timestamp;

    public RecordPK(String name, String period, Date timestamp) {
        this.name = name;
        this.period = period;
        this.timestamp = timestamp;
    }

    public String getName() {
        return name;
    }

    public String getPeriod() {
        return period;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RecordPK recordPK = (RecordPK) o;

        if (name != null ? !name.equals(recordPK.name) : recordPK.name != null) return false;
        if (period != null ? !period.equals(recordPK.period) : recordPK.period != null) return false;
        return timestamp != null ? timestamp.equals(recordPK.timestamp) : recordPK.timestamp == null;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (period != null ? period.hashCode() : 0);
        result = 31 * result + (timestamp != null ? timestamp.hashCode() : 0);
        return result;
    }
}
