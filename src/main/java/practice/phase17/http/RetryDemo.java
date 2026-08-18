package practice.phase17.http;

import org.springframework.http.client.JdkClientHttpRequestFactory;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClient;

import java.time.Duration;

/**
 * 검증용 실험 코드 (직접 타이핑 대상 아님).
 * PricingApiClient.fetchPrice와 동일한 재시도 로직을, 항상 timeout 나는
 * delayMs=3000 URI에 대해 돌려서 3회 재시도 + backoff + 최종 예외를 확인.
 */
public class RetryDemo {

    public static void main(String[] args) {
        JdkClientHttpRequestFactory requestFactory = new JdkClientHttpRequestFactory();
        requestFactory.setReadTimeout(Duration.ofSeconds(1));
        RestClient restClient = RestClient.builder()
                .baseUrl("http://localhost:8090")
                .requestFactory(requestFactory)
                .build();

        System.out.println("=== 항상 timeout (delayMs=3000) ===");
        try {
            callWithRetry(restClient, "/price/PRODUCT-1?delayMs=3000");
        } catch (ResourceAccessException e) {
            System.out.println("main에서 최종 예외 수신: " + e.getClass().getSimpleName());
        }

        System.out.println("=== 정상 (delayMs=100) ===");
        callWithRetry(restClient, "/price/PRODUCT-1?delayMs=100");
    }

    private static void callWithRetry(RestClient restClient, String uri) {
        long start = System.currentTimeMillis();
        int maxAttempts = 3;
        RuntimeException lastError = null;

        for (int attempt = 1; attempt <= maxAttempts; attempt++) {
            try {
                String body = restClient.get().uri(uri).retrieve().body(String.class);
                System.out.println("attempt " + attempt + " 성공: " + body + ", elapsed=" + (System.currentTimeMillis() - start) + "ms");
                return;
            } catch (ResourceAccessException e) {
                lastError = e;
                System.out.println("attempt " + attempt + " 실패: " + e.getClass().getSimpleName() + ", elapsed=" + (System.currentTimeMillis() - start) + "ms");
                try {
                    Thread.sleep(100L * attempt);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                }
            }
        }
        System.out.println("최종 실패, total elapsed=" + (System.currentTimeMillis() - start) + "ms");
        throw lastError;
    }
}
