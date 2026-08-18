package practice.phase17.http;

import org.springframework.http.client.JdkClientHttpRequestFactory;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import practice.phase17.resilience.CircuitBreaker;

import java.time.Duration;

/**
 * 검증용 실험 코드 (직접 타이핑 대상 아님).
 * PricingApiClient.fetchPrice와 동일한 로직을, 항상 500 나는 /down/* 에 대해 돌려서
 * breaker가 OPEN 되는지, OPEN 이후 즉시 fast-fail 되는지 확인.
 */
public class CircuitBreakerDemo {

    public static void main(String[] args) {
        JdkClientHttpRequestFactory requestFactory = new JdkClientHttpRequestFactory();
        requestFactory.setReadTimeout(Duration.ofSeconds(1));
        RestClient restClient = RestClient.builder()
                .baseUrl("http://localhost:8090")
                .requestFactory(requestFactory)
                .build();
        CircuitBreaker breaker = new CircuitBreaker(3, 5000);

        for (int call = 1; call <= 6; call++) {
            System.out.println("--- call " + call + ", state=" + breaker.getState() + " ---");
            callThroughBreaker(restClient, breaker);
        }
    }

    private static void callThroughBreaker(RestClient restClient, CircuitBreaker breaker) {
        long start = System.currentTimeMillis();
        if (!breaker.allowRequest()) {
            System.out.println("fast-fail (circuit open), elapsed=" + (System.currentTimeMillis() - start) + "ms");
            return;
        }

        int maxAttempts = 3;
        RuntimeException lastError = null;
        for (int attempt = 1; attempt <= maxAttempts; attempt++) {
            try {
                String body = restClient.get().uri("/down/x").retrieve().body(String.class);
                System.out.println("attempt " + attempt + " 성공: " + body);
                breaker.recordSuccess();
                return;
            } catch (RestClientException e) {
                lastError = e;
                System.out.println("attempt " + attempt + " 실패: " + e.getClass().getSimpleName());
                try { Thread.sleep(100L * attempt); } catch (InterruptedException ie) { Thread.currentThread().interrupt(); }
            }
        }
        breaker.recordFailure();
        System.out.println("최종 실패(재시도 소진), elapsed=" + (System.currentTimeMillis() - start) + "ms, state=" + breaker.getState());
    }
}
