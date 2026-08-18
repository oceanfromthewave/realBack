package practice.phase6;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@Configuration
@ComponentScan(basePackageClasses = LoggingAspect.class)
@EnableAspectJAutoProxy
public class AspectConfig
{
	@Bean
	public OrderService orderService()
	{
		return new OrderServiceImpl();
	}
}
