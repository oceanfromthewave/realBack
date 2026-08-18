package practice.phase5;

import org.springframework.stereotype.Component;

@Component
public class ServiceA
{
	private final ServiceB serviceB;

	public ServiceA(ServiceB serviceB)
	{
		this.serviceB = serviceB;
		System.out.println("ServiceA created");
	}
}
