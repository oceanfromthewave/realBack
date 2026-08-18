package practice.phase17.cache;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class ProductPriceService
{
	@Cacheable(cacheNames = "productPrice", key = "#productId")
	public int getPrice(String productId)
	{
		System.out.println("[EXTERNAL CALL] " + productId);
		try
		{
			Thread.sleep(80);
		}
		catch(InterruptedException e)
		{
			Thread.currentThread().interrupt();
		}
		return productId.hashCode() % 1000 + 1000;
	}
}
