package practice.phase16.race;

import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertTrue;

class CounterTest {

    @Test
    void increment_underConcurrency_losesUpdates() throws InterruptedException {
        Counter counter = new Counter();
        int threadCount = 10;
        int incrementsPerThread = 1000;
        int expected = threadCount * incrementsPerThread;

        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        CountDownLatch startGate = new CountDownLatch(1); // 스레드들 동시 출발시켜서 경합 최대화
        CountDownLatch doneLatch = new CountDownLatch(threadCount);

        for (int i = 0; i < threadCount; i++) {
            executor.submit(() -> {
                try {
                    startGate.await();
                    for (int j = 0; j < incrementsPerThread; j++) {
                        counter.increment();
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

        // count++가 원자적이지 않아 lost update 발생 -> 기대값(10000)보다 작게 나온다
        assertTrue(counter.get() < expected,
                "expected lost updates (< " + expected + "), got: " + counter.get());
    }
}
