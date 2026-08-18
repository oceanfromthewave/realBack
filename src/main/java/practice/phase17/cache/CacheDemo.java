package practice.phase17.cache;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/** 검증용 실험 코드 (직접 타이핑 대상 아님). */
public class CacheDemo {
    public static void main(String[] args) {
        try (var ctx = new AnnotationConfigApplicationContext(CacheConfig.class, ProductPriceService.class)) {
            ProductPriceService service = ctx.getBean(ProductPriceService.class);

            long start = System.currentTimeMillis();
            for (int i = 0; i < 5; i++) {
                int price = service.getPrice("PRODUCT-1");
                System.out.println("price=" + price);
            }
            int other = service.getPrice("PRODUCT-2");
            System.out.println("other price=" + other);
            System.out.println("elapsed=" + (System.currentTimeMillis() - start) + "ms");
        }
    }
}
