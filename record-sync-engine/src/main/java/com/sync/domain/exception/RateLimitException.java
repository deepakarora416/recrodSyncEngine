package com.sync.domain.exception;

/**
 * Thrown when external API rate limit is exceeded.
 */
public class RateLimitException extends SyncException {

    private final long retryAfterMs;

    public RateLimitException(String providerId, long retryAfterMs) {
        super("Rate limit exceeded for provider: " + providerId);
        this.retryAfterMs = retryAfterMs;
    }

    public long getRetryAfterMs() {
        return retryAfterMs;
    }
}
