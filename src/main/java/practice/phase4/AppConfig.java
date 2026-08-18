package practice.phase4;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

@Configuration
@PropertySource("classpath:phase4.properties")
@EnableConfigurationProperties(NotificationProperties.class)
class AppConfig
{
	@Bean
	static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer()
	{
		return new PropertySourcesPlaceholderConfigurer();
	}

	@Bean
	@Profile("dev")
	Notifier notifier(NotificationProperties props)
	{
		System.out.println("sender(@ConfigurationProperties)=" + props.getSender());
		return new EmailNotifier();
	}

	@Bean
	@Profile("prod")
	Notifier prodNotifier(@Value("${notification.sender}") String sender)
	{
		System.out.println("sender(@Value)=" + sender);
		return new SmsNotifier();
	}

	@Bean
	OrderService orderService(Notifier notifier)
	{
		return new OrderService(notifier);
	}
}
