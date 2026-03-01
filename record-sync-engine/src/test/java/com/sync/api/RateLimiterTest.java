package com.sync.api;

import org.junit.Test;

import static org.junit.Assert.*;

public class RateLimiterTest {

    @Test
    public void testInitialTokensAvailable() {
        TokenBucketRateLimiter limiter = new TokenBucketRateLimiter(5, 1000);
        assertEquals(5, limiter.getAvailablePermits());
    }

    @Test
    public void testTokensConsumed() {
        TokenBucketRateLimiter limiter = new TokenBucketRateLimiter(5, 1000);

        assertTrue(limiter.tryAcquire());
        assertTrue(limiter.tryAcquire());
        assertTrue(limiter.tryAcquire());

        assertEquals(2, limiter.getAvailablePermits());
    }

    @Test
    public void testRateLimitExceeded() {
        TokenBucketRateLimiter limiter = new TokenBucketRateLimiter(2, 1000);

        assertTrue(limiter.tryAcquire());
        assertTrue(limiter.tryAcquire());
        assertFalse(limiter.tryAcquire());
    }

    @Test
    public void testWaitTimeWhenEmpty() {
        TokenBucketRateLimiter limiter = new TokenBucketRateLimiter(1, 1000);

        limiter.tryAcquire();
        long waitTime = limiter.getWaitTimeMs();

        assertTrue(waitTime > 0);
        assertTrue(waitTime <= 1000);
    }

    @Test
    public void testNoWaitTimeWhenTokensAvailable() {
        TokenBucketRateLimiter limiter = new TokenBucketRateLimiter(5, 1000);
        assertEquals(0, limiter.getWaitTimeMs());
    }

    @Test
    public void testTokenRefill() throws InterruptedException {
        TokenBucketRateLimiter limiter = new TokenBucketRateLimiter(2, 100);

        assertTrue(limiter.tryAcquire());
        assertTrue(limiter.tryAcquire());
        assertFalse(limiter.tryAcquire());

        Thread.sleep(150);

        assertTrue(limiter.tryAcquire());
    }
}
