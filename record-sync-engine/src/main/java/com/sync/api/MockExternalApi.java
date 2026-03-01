package com.sync.api;

import com.sync.domain.exception.RateLimitException;
import com.sync.domain.model.ExternalRecord;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Simulates external CRM API with in-memory storage, rate limiting, and latency.
 */
public class MockExternalApi {

    private final String providerId;
    private final RateLimiter rateLimiter;
    private final Map<String, ExternalRecord> storage = new ConcurrentHashMap<>();
    private final int simulatedLatencyMs;

    public MockExternalApi(String providerId, int requestsPerSecond, int simulatedLatencyMs) {
        this.providerId = providerId;
        this.rateLimiter = new TokenBucketRateLimiter(requestsPerSecond, 1000);
        this.simulatedLatencyMs = simulatedLatencyMs;
    }

    public ExternalRecord create(ExternalRecord record) {
        checkRateLimit();
        simulateLatency();
        storage.put(record.getExternalId(), record);
        return record;
    }

    public Optional<ExternalRecord> read(String externalId) {
        checkRateLimit();
        simulateLatency();
        return Optional.ofNullable(storage.get(externalId));
    }

    public ExternalRecord update(ExternalRecord record) {
        checkRateLimit();
        simulateLatency();
        if (!storage.containsKey(record.getExternalId())) {
            throw new IllegalArgumentException("Record not found: " + record.getExternalId());
        }
        storage.put(record.getExternalId(), record);
        return record;
    }

    public boolean delete(String externalId) {
        checkRateLimit();
        simulateLatency();
        return storage.remove(externalId) != null;
    }

    private void checkRateLimit() {
        if (!rateLimiter.tryAcquire()) {
            throw new RateLimitException(providerId, rateLimiter.getWaitTimeMs());
        }
    }

    private void simulateLatency() {
        if (simulatedLatencyMs > 0) {
            try {
                Thread.sleep(simulatedLatencyMs);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
