package practice.phase17.problem;

/**
 * Phase 17 문제 제시용 실험 코드 (직접 타이핑 대상 아님).
 * 외부 API(가격 조회)가 느리다고 가정. 캐시 없이 같은 상품 가격을
 * 반복 조회하면 매번 그 느린 호출을 다시 탄다.
 */
public class SlowExternalCallDemo {

    // 외부 API 흉내: 항상 80ms 걸림
    static int fetchPriceFromExternalApi(String productId) {
        try {
            Thread.sleep(80);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return productId.hashCode() % 1000 + 1000;
    }

    public static void main(String[] args) {
        String productId = "PRODUCT-1";
        long start = System.currentTimeMillis();

        // 같은 상품 가격을 100번 조회 (실무: 상품 상세 페이지 100 view)
        for (int i = 0; i < 100; i++) {
            fetchPriceFromExternalApi(productId);
        }

        long elapsed = System.currentTimeMillis() - start;
        System.out.println("캐시 없이 100회 조회: " + elapsed + "ms");
        System.out.println("-> 캐시 있으면 이론상 1회(80ms) + 나머지는 메모리 조회(~0ms)");
    }
}
