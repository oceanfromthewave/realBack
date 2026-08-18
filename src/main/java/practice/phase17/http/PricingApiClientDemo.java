package practice.phase17.http;

/** 검증용 실험 코드 (직접 타이핑 대상 아님). PricingMockServer가 :8090에서 떠 있어야 함. */
public class PricingApiClientDemo {
    public static void main(String[] args) {
        PricingApiClient client = new PricingApiClient();
        PricingApiClient.PriceResponse response = client.fetchPrice("PRODUCT-1");
        System.out.println(response);
    }
}
