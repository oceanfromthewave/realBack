package practice.phase3;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class ContextAwareBean implements ApplicationContextAware
{
	private ApplicationContext context;

	@Override
	public void setApplicationContext(ApplicationContext context)
	{
		this.context = context;
		System.out.println("[Aware] context injected: " + context.getClass().getSimpleName());
	}
}
