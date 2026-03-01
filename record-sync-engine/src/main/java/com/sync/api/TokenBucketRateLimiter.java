package com.sync.api;

/**
 * Token bucket rate limiter. Allows bursts up to maxTokens, refills over time.
 */
public class TokenBucketRateLimiter implements RateLimiter {

    private final int maxTokens;
    private final long refillIntervalMs;
    private int availableTokens;
    private long lastRefillTime;

    public TokenBucketRateLimiter(int maxTokens, long refillIntervalMs) {
        this.maxTokens = maxTokens;
        this.refillIntervalMs = refillIntervalMs;
        this.availableTokens = maxTokens;
        this.lastRefillTime = System.currentTimeMillis();
    }

    @Override
    public synchronized boolean tryAcquire() {
        refillTokens();
        if (availableTokens > 0) {
            availableTokens--;
            return true;
        }
        return false;
    }

    @Override
    public synchronized void acquire() throws InterruptedException {
        while (!tryAcquire()) {
            long waitTime = getWaitTimeMs();
            if (waitTime > 0) {
                Thread.sleep(waitTime);
            }
        }
    }

    @Override
    public synchronized long getWaitTimeMs() {
        if (availableTokens > 0) {
            return 0;
        }
        long elapsed = System.currentTimeMillis() - lastRefillTime;
        return Math.max(0, refillIntervalMs - elapsed);
    }

    @Override
    public synchronized int getAvailablePermits() {
        refillTokens();
        return availableTokens;
    }

    private void refillTokens() {
        long now = System.currentTimeMillis();
        long elapsed = now - lastRefillTime;

        if (elapsed >= refillIntervalMs) {
            int tokensToAdd = (int) (elapsed / refillIntervalMs);
            availableTokens = Math.min(maxTokens, availableTokens + tokensToAdd);
            lastRefillTime = now;
        }
    }
}
