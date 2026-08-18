package practice.phase16.race;

import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SafeCounterTest {

    private static final int THREAD_COUNT = 10;
    private static final int INCREMENTS_PER_THREAD = 1000;
    private static final int EXPECTED = THREAD_COUNT * INCREMENTS_PER_THREAD;

    @Test
    void incrementWithLock_underConcurrency_isExact() throws InterruptedException {
        SafeCounter counter = new SafeCounter();
        runConcurrently(counter::incrementWithLock);
        assertEquals(EXPECTED, counter.getSyncCount());
    }

    @Test
    void incrementWithAtomic_underConcurrency_isExact() throws InterruptedException {
        SafeCounter counter = new SafeCounter();
        runConcurrently(counter::incrementWithAtomic);
        assertEquals(EXPECTED, counter.getAtomicCount());
    }

    private void runConcurrently(Runnable incrementCall) throws InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(THREAD_COUNT);
        CountDownLatch startGate = new CountDownLatch(1);
        CountDownLatch doneLatch = new CountDownLatch(THREAD_COUNT);

        for (int i = 0; i < THREAD_COUNT; i++) {
            executor.submit(() -> {
                try {
                    startGate.await();
                    for (int j = 0; j < INCREMENTS_PER_THREAD; j++) {
                        incrementCall.run();
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    doneLatch.countDown();
                }
            });
        }

        startGate.countDown();
        doneLatch.await(10, TimeUnit.SECONDS);
        executor.shutdown();
    }
}
