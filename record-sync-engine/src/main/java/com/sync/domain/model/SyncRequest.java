package com.sync.domain.model;

import java.util.Objects;
import java.util.UUID;

/**
 * Encapsulates a single sync operation with direction, operation type, and record.
 */
public class SyncRequest {

    private final String requestId;
    private final String providerId;
    private final SyncOperation operation;
    private final SyncDirection direction;
    private final InternalRecord internalRecord;
    private final ExternalRecord externalRecord;

    private SyncRequest(Builder builder) {
        this.requestId = UUID.randomUUID().toString();
        this.providerId = Objects.requireNonNull(builder.providerId);
        this.operation = Objects.requireNonNull(builder.operation);
        this.direction = Objects.requireNonNull(builder.direction);
        this.internalRecord = builder.internalRecord;
        this.externalRecord = builder.externalRecord;

        if (direction == SyncDirection.OUTBOUND && internalRecord == null) {
            throw new IllegalArgumentException("Internal record required for outbound sync");
        }
        if (direction == SyncDirection.INBOUND && externalRecord == null) {
            throw new IllegalArgumentException("External record required for inbound sync");
        }
    }

    public String getRequestId() {
        return requestId;
    }

    public String getProviderId() {
        return providerId;
    }

    public SyncOperation getOperation() {
        return operation;
    }

    public SyncDirection getDirection() {
        return direction;
    }

    public InternalRecord getInternalRecord() {
        return internalRecord;
    }

    public ExternalRecord getExternalRecord() {
        return externalRecord;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String providerId;
        private SyncOperation operation;
        private SyncDirection direction;
        private InternalRecord internalRecord;
        private ExternalRecord externalRecord;

        public Builder providerId(String providerId) {
            this.providerId = providerId;
            return this;
        }

        public Builder operation(SyncOperation operation) {
            this.operation = operation;
            return this;
        }

        public Builder direction(SyncDirection direction) {
            this.direction = direction;
            return this;
        }

        public Builder internalRecord(InternalRecord record) {
            this.internalRecord = record;
            return this;
        }

        public Builder externalRecord(ExternalRecord record) {
            this.externalRecord = record;
            return this;
        }

        public SyncRequest build() {
            return new SyncRequest(this);
        }
    }

    @Override
    public String toString() {
        return "SyncRequest{id='" + requestId + "', op=" + operation + ", dir=" + direction + "}";
    }
}
