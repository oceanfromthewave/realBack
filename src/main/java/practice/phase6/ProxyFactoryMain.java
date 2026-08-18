package practice.phase6;

import org.springframework.aop.framework.ProxyFactory;

public class ProxyFactoryMain
{

	public static void main(String[] args)
	{
		OrderServiceImpl target = new OrderServiceImpl();

		ProxyFactory proxyFactory = new ProxyFactory(target);
		proxyFactory.addAdvice(new LoggingAdvice());

		OrderService proxy = (OrderService) proxyFactory.getProxy();

		proxy.placeOrder("C-001");
		proxy.cancelOrder("C-001");

		System.out.println("target implements interface -> " + (target instanceof OrderService));
		System.out.println("proxy class = " + proxy.getClass());
	}
}
