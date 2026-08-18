package practice.phase17.idempotency;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import practice.phase17.cache.CacheConfig;

/** 검증용 실험 코드 (직접 타이핑 대상 아님). Redis(:6379) 떠 있어야 함. */
public class PaymentServiceDemo {
    public static void main(String[] args) {
        try (AnnotationConfigApplicationContext ctx =
                     new AnnotationConfigApplicationContext(CacheConfig.class, PaymentService.class)) {
            PaymentService service = ctx.getBean(PaymentService.class);

            System.out.println("=== key-1 첫 호출 ===");
            System.out.println(service.pay("key-1", "ORDER-1", 1000));

            System.out.println("=== key-1 재호출 (재시도 흉내) ===");
            System.out.println(service.pay("key-1", "ORDER-1", 1000));

            System.out.println("=== key-2 (다른 요청) ===");
            System.out.println(service.pay("key-2", "ORDER-2", 2000));
        }
    }
}
