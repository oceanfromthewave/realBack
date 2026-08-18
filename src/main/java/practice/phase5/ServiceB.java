package practice.phase5;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
public class ServiceB
{
	private final ServiceA serviceA;

	public ServiceB(@Lazy ServiceA serviceA)
	{
		this.serviceA = serviceA;
		System.out.println("ServiceB created");
	}
}
