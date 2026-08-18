package practice.phase3;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.stereotype.Component;

@Component
public class LifecycleDemoBean
{
	public LifecycleDemoBean()
	{
		System.out.println("1. 생성자 호출");
	}

	@PostConstruct
	public void init()
	{
		System.out.println("2. @PostConstruct");
	}

	@PreDestroy
	public void destroy()
	{
		System.out.println("3. @PreDestroy");
	}
}
