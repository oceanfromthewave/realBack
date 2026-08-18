package practice.phase5;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
@ComponentScan
public class AppConfig
{
	@Bean
	@Scope("prototype")
	Cart cart()
	{
		return new Cart();
	}
}
