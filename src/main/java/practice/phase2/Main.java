package practice.phase2;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan
class AppConfig
{
}

public class Main
{
	public static void main(String[] args)
	{
		var context = new AnnotationConfigApplicationContext(AppConfig.class);
		OrderService service = context.getBean(OrderService.class);
		service.placeOrder("Book");
		context.close();
	}
}
