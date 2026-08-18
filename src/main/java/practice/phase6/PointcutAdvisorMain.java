package practice.phase6;

import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.NameMatchMethodPointcut;
import org.springframework.aop.framework.ProxyFactory;

public class PointcutAdvisorMain {

	public static void main(String[] args) {
		OrderServiceImpl target = new OrderServiceImpl();

		NameMatchMethodPointcut pointcut = new NameMatchMethodPointcut();
		pointcut.addMethodName("placeOrder");

		DefaultPointcutAdvisor advisor = new DefaultPointcutAdvisor(pointcut, new LoggingAdvice());

		ProxyFactory proxyFactory = new ProxyFactory(target);
		proxyFactory.addAdvisor(advisor);

		OrderService proxy = (OrderService) proxyFactory.getProxy();

		proxy.placeOrder("D-001");
		proxy.cancelOrder("D-001");
	}
}
