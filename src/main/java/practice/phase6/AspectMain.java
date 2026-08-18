package practice.phase6;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class AspectMain
{
	public static void main(String[] args)
	{
		var context = new AnnotationConfigApplicationContext(AspectConfig.class);

		OrderService orderService = context.getBean(OrderService.class);

		System.out.println("bean class = " + orderService.getClass());

		orderService.placeOrder("E-001");
		orderService.cancelOrder("E-001");

		context.close();
	}
}
