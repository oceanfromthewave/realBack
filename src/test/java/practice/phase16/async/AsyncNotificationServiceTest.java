package practice.phase16.async;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AsyncNotificationServiceTest {

    private AnnotationConfigApplicationContext context;

    @AfterEach
    void closeContext() {
        if (context != null) {
            context.close();
        }
    }

    @Test
    void send_runsOnAsyncExecutorThread_notCallerThread() throws Exception {
        context = new AnnotationConfigApplicationContext(AsyncConfig.class, AsyncNotificationService.class);
        AsyncNotificationService service = context.getBean(AsyncNotificationService.class);
        String callerThread = Thread.currentThread().getName();

        CompletableFuture<String> future = service.send("hello");

        // 메서드 호출 자체는 원본 sleep(500ms) 안 기다리고 바로 리턴돼야 함 (proxy가 executor한테 던짐)
        String result = future.get(2, TimeUnit.SECONDS);

        assertTrue(result.contains("async-"), "expected async- prefixed thread, got: " + result);
        assertTrue(result.startsWith("hello sent by"));
        assertNotEquals(callerThread, extractThreadName(result));
    }

    private String extractThreadName(String result) {
        return result.substring(result.lastIndexOf(" ") + 1);
    }
}
