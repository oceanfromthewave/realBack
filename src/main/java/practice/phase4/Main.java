package practice.phase4;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Main
{
	public static void main(String[] args)
	{
		var context = new AnnotationConfigApplicationContext();
		context.getEnvironment().setActiveProfiles("dev");
		context.register(AppConfig.class);
		context.refresh();

		OrderService service = context.getBean(OrderService.class);
		service.placeOrder("Book");
		context.close();
	}
}
