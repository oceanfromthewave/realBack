package practice.phase16.executor;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TaskRunnerTest {

    @Test
    void runAll_returnsResultForEveryTask_inSubmittedOrder() throws InterruptedException {
        TaskRunner runner = new TaskRunner(Executors.newFixedThreadPool(2));

        List<Callable<String>> tasks = IntStream.rangeClosed(1, 5)
                .<Callable<String>>mapToObj(i -> () -> "task-" + i)
                .collect(Collectors.toList());

        List<String> results = runner.runAll(tasks);

        assertEquals(List.of("task-1", "task-2", "task-3", "task-4", "task-5"), results);
    }

    @Test
    void runAll_reusesThreads_fromFixedPool() throws InterruptedException {
        TaskRunner runner = new TaskRunner(Executors.newFixedThreadPool(2));

        List<Callable<String>> tasks = IntStream.rangeClosed(1, 5)
                .<Callable<String>>mapToObj(i -> (Callable<String>) () -> Thread.currentThread().getName())
                .collect(Collectors.toList());

        List<String> threadNames = runner.runAll(tasks);
        Set<String> distinctThreads = Set.copyOf(threadNames);

        // pool size 2 -> task 5개 처리해도 실제 쓰인 스레드는 2개 이하
        assertTrue(distinctThreads.size() <= 2, "expected pool reuse, got threads: " + distinctThreads);
    }

    @Test
    void runAll_shutsDownExecutor_afterCompletion() throws InterruptedException {
        var executor = Executors.newFixedThreadPool(2);
        TaskRunner runner = new TaskRunner(executor);

        runner.runAll(List.of(() -> "ok"));

        assertTrue(executor.isShutdown());
        assertTrue(executor.isTerminated());
    }

    @Test
    void runAll_capturesException_asErrorResult() throws InterruptedException {
        TaskRunner runner = new TaskRunner(Executors.newFixedThreadPool(1));
        AtomicInteger calls = new AtomicInteger();

        List<Callable<String>> tasks = List.of(
                () -> { calls.incrementAndGet(); throw new RuntimeException("boom"); },
                () -> { calls.incrementAndGet(); return "ok"; }
        );

        List<String> results = runner.runAll(tasks);

        assertTrue(results.get(0).startsWith("ERROR"));
        assertEquals("ok", results.get(1));
        assertEquals(2, calls.get());
    }
}
