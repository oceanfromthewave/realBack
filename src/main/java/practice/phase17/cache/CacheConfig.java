package practice.phase17.cache;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.time.Duration;

@Configuration
@EnableCaching
public class CacheConfig
{
	@Bean
	public LettuceConnectionFactory redisConnectionFactory()
	{
		return new LettuceConnectionFactory("localhost", 6379);
	}

	@Bean
	public CacheManager cacheManager(LettuceConnectionFactory connectionFactory)
	{
		RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofSeconds(30));

		return RedisCacheManager.builder(connectionFactory).cacheDefaults(config).build();
	}

	@Bean
	public StringRedisTemplate redisTemplate(LettuceConnectionFactory connectionFactory)
	{
		return new StringRedisTemplate(connectionFactory);
	}
}
