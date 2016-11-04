package edu.kafka.storage.domain;

public enum AggregationTime {
    MIN5("5 min"), HOURLY("hourly"), DAILY("daily");

    private String time;

    AggregationTime(String time) {
        this.time = time;
    }

    public String getValue() {
        return time;
    }
}
