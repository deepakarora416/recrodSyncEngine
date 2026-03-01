package com.sync.domain.model;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Record from our internal system. Uses Map for flexible field storage.
 */
public class InternalRecord {

    private final String id;
    private final String recordType;
    private final Map<String, Object> fields;
    private final Instant createdAt;
    private Instant updatedAt;
    private long version;

    public InternalRecord(String id, String recordType) {
        this.id = Objects.requireNonNull(id);
        this.recordType = Objects.requireNonNull(recordType);
        this.fields = new HashMap<>();
        this.createdAt = Instant.now();
        this.updatedAt = this.createdAt;
        this.version = 1;
    }

    public String getId() {
        return id;
    }

    public String getRecordType() {
        return recordType;
    }

    public Map<String, Object> getFields() {
        return new HashMap<>(fields);
    }

    public void setField(String key, Object value) {
        this.fields.put(key, value);
        this.updatedAt = Instant.now();
    }

    public Object getField(String key) {
        return this.fields.get(key);
    }

    public boolean hasField(String key) {
        return this.fields.containsKey(key);
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public long getVersion() {
        return version;
    }

    public void incrementVersion() {
        this.version++;
        this.updatedAt = Instant.now();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InternalRecord that = (InternalRecord) o;
        return Objects.equals(id, that.id) && Objects.equals(recordType, that.recordType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, recordType);
    }

    @Override
    public String toString() {
        return "InternalRecord{id='" + id + "', type='" + recordType + "', version=" + version + "}";
    }
}
