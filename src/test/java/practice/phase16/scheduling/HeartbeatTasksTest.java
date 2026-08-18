package practice.phase16.scheduling;

import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.assertTrue;

class HeartbeatTasksTest {

    // SchedulingConfig에 ThreadPoolTaskScheduler(pool 4) Bean 등록 후
    // fast/slow 두 @Scheduled 메서드가 서로 다른 스레드에서 병렬로 돈다.
    @Test
    void pooledScheduler_runsTasks_onMultipleThreads() throws InterruptedException {
        PrintStream originalOut = System.out;
        ByteArrayOutputStream captured = new ByteArrayOutputStream();
        System.setOut(new PrintStream(captured));

        AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext(SchedulingConfig.class, HeartbeatTasks.class);
        try {
            Thread.sleep(4000); // slowTask 한 사이클(3초) + fastTask 몇 번 돌 시간
        } finally {
            context.close();
            System.setOut(originalOut);
        }

        String log = captured.toString();
        Set<String> threadNames = extractThreadNames(log);

        // pool 4짜리 스케줄러라 slowTask(3초)가 fastTask 실행을 막지 않아야 함 -> 스레드 2개 이상 관측
        assertTrue(threadNames.size() >= 2, "expected multiple scheduler threads, got: " + threadNames);
    }

    private Set<String> extractThreadNames(String log) {
        Set<String> names = new HashSet<>();
        Matcher matcher = Pattern.compile("on (\\S+)").matcher(log);
        while (matcher.find()) {
            names.add(matcher.group(1));
        }
        return names;
    }
}
