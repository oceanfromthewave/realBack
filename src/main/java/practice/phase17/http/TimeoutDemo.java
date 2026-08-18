package practice.phase17.http;

import org.springframework.http.client.JdkClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

import java.time.Duration;

/** 검증용 실험 코드 (직접 타이핑 대상 아님). PricingApiClient의 1초 read timeout 확인. */
public class TimeoutDemo {

    public static void main(String[] args) {
        JdkClientHttpRequestFactory requestFactory = new JdkClientHttpRequestFactory();
        requestFactory.setReadTimeout(Duration.ofSeconds(1));
        RestClient restClient = RestClient.builder()
                .baseUrl("http://localhost:8090")
                .requestFactory(requestFactory)
                .build();

        call(restClient, 500);   // 타임아웃(1초) 이내 -> 정상
        call(restClient, 3000);  // 타임아웃(1초) 초과 -> 예외
    }

    private static void call(RestClient restClient, long delayMs) {
        long start = System.currentTimeMillis();
        try {
            String body = restClient.get()
                    .uri("/price/PRODUCT-1?delayMs=" + delayMs)
                    .retrieve()
                    .body(String.class);
            System.out.println("delayMs=" + delayMs + " -> 응답 " + body + ", elapsed=" + (System.currentTimeMillis() - start) + "ms");
        } catch (Exception e) {
            System.out.println("delayMs=" + delayMs + " -> 예외 " + e.getClass().getSimpleName() + ", elapsed=" + (System.currentTimeMillis() - start) + "ms");
        }
    }
}
