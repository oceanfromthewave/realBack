package practice.phase6;

import java.lang.reflect.Proxy;

public class Main
{

	public static void main(String[] args)
	{
		OrderService target = new OrderServiceImpl();

		OrderService proxy = (OrderService) Proxy.newProxyInstance(OrderService.class.getClassLoader(), new Class[] { OrderService.class },
				new LoggingInvocationHandler(target));

		proxy.placeOrder("A-001");
		proxy.cancelOrder("A-001");

		System.out.println("proxy class = " + proxy.getClass());
	}
}
