package practice.phase17.http;

import org.springframework.http.client.JdkClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import practice.phase17.resilience.CircuitBreaker;

import java.time.Duration;

@Component
public class PricingApiClient
{

	private final RestClient restClient;
	private final CircuitBreaker circuitBreaker = new CircuitBreaker(3, 5000);

	public PricingApiClient()
	{
		JdkClientHttpRequestFactory requestFactory = new JdkClientHttpRequestFactory();
		requestFactory.setReadTimeout(Duration.ofSeconds(1));

		this.restClient = RestClient.builder().baseUrl("http://localhost:8090").requestFactory(requestFactory).build();
	}

	public PriceResponse fetchPrice(String productId)
	{
		if(!circuitBreaker.allowRequest())
		{
			throw new IllegalStateException("circuit open, fast fail for " + productId);
		}

		int maxAttempts = 3;
		RuntimeException lastError = null;

		for(int attempt = 1; attempt <= maxAttempts; attempt++)
		{
			try
			{
				PriceResponse response = restClient.get().uri("/price/{productId}", productId).retrieve().body(PriceResponse.class);
				circuitBreaker.recordSuccess();
				return response;
			}
			catch(RestClientException e)
			{
				lastError = e;
				System.out.println("[RETRY] attempt " + attempt + " failed: " + e.getClass().getSimpleName());
				sleepBackoff(attempt);
			}
		}
		circuitBreaker.recordFailure();
		throw lastError;
	}

	private void sleepBackoff(int attempt)
	{
		try
		{
			Thread.sleep(100L * attempt); // 100ms, 200ms, 300ms...
		}
		catch(InterruptedException e)
		{
			Thread.currentThread().interrupt();
		}
	}

	public record PriceResponse(String productId, int price)
	{
	}
}