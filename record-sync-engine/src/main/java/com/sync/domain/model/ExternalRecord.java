package com.sync.domain.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Record from an external CRM. Tracks provider origin and links back to internal ID.
 */
public class ExternalRecord {

    private final String externalId;
    private final String providerId;
    private final String entityType;
    private final Map<String, Object> data;
    private String internalReferenceId;

    public ExternalRecord(String externalId, String providerId, String entityType) {
        this.externalId = Objects.requireNonNull(externalId);
        this.providerId = Objects.requireNonNull(providerId);
        this.entityType = Objects.requireNonNull(entityType);
        this.data = new HashMap<>();
    }

    public String getExternalId() {
        return externalId;
    }

    public String getProviderId() {
        return providerId;
    }

    public String getEntityType() {
        return entityType;
    }

    public Map<String, Object> getData() {
        return new HashMap<>(data);
    }

    public void setData(String key, Object value) {
        this.data.put(key, value);
    }

    public Object getData(String key) {
        return this.data.get(key);
    }

    public String getInternalReferenceId() {
        return internalReferenceId;
    }

    public void setInternalReferenceId(String internalReferenceId) {
        this.internalReferenceId = internalReferenceId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ExternalRecord that = (ExternalRecord) o;
        return Objects.equals(externalId, that.externalId) && Objects.equals(providerId, that.providerId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(externalId, providerId);
    }

    @Override
    public String toString() {
        return "ExternalRecord{id='" + externalId + "', provider='" + providerId + "'}";
    }
}
