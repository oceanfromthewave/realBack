package practice.phase16.txasync;

import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertTrue;

class TransactionalCallerTest {

    @Test
    void asyncWork_doesNotSeeCallersTransaction() throws InterruptedException {
        PrintStream originalOut = System.out;
        ByteArrayOutputStream captured = new ByteArrayOutputStream();
        System.setOut(new PrintStream(captured));

        AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext(TxAsyncConfig.class, TransactionalCaller.class, AsyncWorker.class);
        try {
            TransactionalCaller caller = context.getBean(TransactionalCaller.class);
            boolean activeInCallerThread = caller.runWithTransaction();

            assertTrue(activeInCallerThread, "caller thread should be inside an active transaction");

            Thread.sleep(500); // async 스레드가 로그 찍을 시간 확보
        } finally {
            context.close();
            System.setOut(originalOut);
        }

        String log = captured.toString();
        assertTrue(log.contains("[caller]") && log.contains("txActive=true"), "caller log missing/wrong: " + log);
        assertTrue(log.contains("txActive=false"), "async thread should NOT see caller's transaction: " + log);
    }
}
