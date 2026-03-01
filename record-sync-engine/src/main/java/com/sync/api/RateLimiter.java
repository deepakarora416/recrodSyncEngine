package com.sync.api;

/**
 * Rate limiting contract for API calls.
 */
public interface RateLimiter {

    boolean tryAcquire();

    void acquire() throws InterruptedException;

    long getWaitTimeMs();

    int getAvailablePermits();
}
