package com.example.cashservice.dto;

import java.time.Instant;
import java.util.Map;
import java.util.Objects;

public class NotificationEvent {
    private String key;
    private String type;
    private String aggregateId;
    private Instant happenedAt;
    private Map<String, Object> payload;

    public NotificationEvent() { }

    public NotificationEvent(String key, String type, String aggregateId, Instant happenedAt, Map<String, Object> payload) {
        this.key = key;
        this.type = type;
        this.aggregateId = aggregateId;
        this.happenedAt = happenedAt;
        this.payload = payload;
    }

    /** Ключ для send(topic, key, value) */
    public String getKey() { return key; }
    public void setKey(String key) { this.key = key; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getAggregateId() { return aggregateId; }
    public void setAggregateId(String aggregateId) { this.aggregateId = aggregateId; }

    public Instant getHappenedAt() { return happenedAt; }
    public void setHappenedAt(Instant happenedAt) { this.happenedAt = happenedAt; }

    public Map<String, Object> getPayload() { return payload; }
    public void setPayload(Map<String, Object> payload) { this.payload = payload; }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof NotificationEvent)) return false;
        NotificationEvent that = (NotificationEvent) o;
        return Objects.equals(key, that.key) &&
                Objects.equals(type, that.type) &&
                Objects.equals(aggregateId, that.aggregateId) &&
                Objects.equals(happenedAt, that.happenedAt) &&
                Objects.equals(payload, that.payload);
    }
    @Override public int hashCode() { return Objects.hash(key, type, aggregateId, happenedAt, payload); }
    @Override public String toString() {
        return "NotificationEvent{key='" + key + "', type='" + type + "', aggregateId='" + aggregateId + "'}";
    }
}
