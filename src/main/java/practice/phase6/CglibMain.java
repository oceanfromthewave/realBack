package practice.phase6;

import org.springframework.cglib.proxy.Enhancer;

public class CglibMain
{
	public static void main(String[] args)
	{
		Enhancer enhancer = new Enhancer();
		enhancer.setSuperclass(OrderServiceImpl.class);
		enhancer.setCallback(new LoggingMethodInterceptor());

		OrderServiceImpl proxy = (OrderServiceImpl) enhancer.create();

		proxy.placeOrder("B-001");
		proxy.cancelOrder("B-001");

		System.out.println("proxy class = " + proxy.getClass());
		System.out.println("superclass = " + proxy.getClass().getSuperclass());
	}
}
