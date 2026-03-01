package com.sync.domain.model;

/**
 * Outcome of a sync operation with success/failure status and resulting records.
 */
public class SyncResult {

    private final String requestId;
    private final boolean success;
    private final String message;
    private final InternalRecord resultingInternalRecord;
    private final ExternalRecord resultingExternalRecord;
    private final long processingTimeMs;

    private SyncResult(Builder builder) {
        this.requestId = builder.requestId;
        this.success = builder.success;
        this.message = builder.message;
        this.resultingInternalRecord = builder.resultingInternalRecord;
        this.resultingExternalRecord = builder.resultingExternalRecord;
        this.processingTimeMs = builder.processingTimeMs;
    }

    public String getRequestId() {
        return requestId;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public InternalRecord getResultingInternalRecord() {
        return resultingInternalRecord;
    }

    public ExternalRecord getResultingExternalRecord() {
        return resultingExternalRecord;
    }

    public long getProcessingTimeMs() {
        return processingTimeMs;
    }

    public static Builder success(String requestId) {
        return new Builder().requestId(requestId).success(true);
    }

    public static Builder failure(String requestId, String message) {
        return new Builder().requestId(requestId).success(false).message(message);
    }

    public static class Builder {
        private String requestId;
        private boolean success;
        private String message;
        private InternalRecord resultingInternalRecord;
        private ExternalRecord resultingExternalRecord;
        private long processingTimeMs;

        public Builder requestId(String requestId) {
            this.requestId = requestId;
            return this;
        }

        public Builder success(boolean success) {
            this.success = success;
            return this;
        }

        public Builder message(String message) {
            this.message = message;
            return this;
        }

        public Builder resultingInternalRecord(InternalRecord record) {
            this.resultingInternalRecord = record;
            return this;
        }

        public Builder resultingExternalRecord(ExternalRecord record) {
            this.resultingExternalRecord = record;
            return this;
        }

        public Builder processingTimeMs(long ms) {
            this.processingTimeMs = ms;
            return this;
        }

        public SyncResult build() {
            return new SyncResult(this);
        }
    }

    @Override
    public String toString() {
        return "SyncResult{success=" + success + ", message='" + message + "', time=" + processingTimeMs + "ms}";
    }
}
