package practice.phase17.idempotency;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class PaymentService
{
	private static final String PROCESSING = "PROCESSING";

	private final StringRedisTemplate redisTemplate;

	public PaymentService(StringRedisTemplate redisTemplate)
	{
		this.redisTemplate = redisTemplate;
	}

	public String pay(String idempotencyKey, String orderId, int amount)
	{
		String redisKey = "idem:" + idempotencyKey;
		Boolean claimed = redisTemplate.opsForValue().setIfAbsent(redisKey, PROCESSING, Duration.ofSeconds(60));

		if(Boolean.FALSE.equals(claimed))
		{
			String existing = redisTemplate.opsForValue().get(redisKey);
			if(PROCESSING.equals(existing))
			{
				throw new IllegalStateException("payment already in flight for key " + idempotencyKey);
			}
			return existing;
		}

		String result = doCharge(orderId, amount);
		redisTemplate.opsForValue().set(redisKey, result, Duration.ofHours(24));
		return result;
	}

	private String doCharge(String orderId, int amount)
	{
		System.out.println("[CHARGE] order=" + orderId + ", amount=" + amount);
		return "PAID:" + orderId + ":" + amount;
	}
}
