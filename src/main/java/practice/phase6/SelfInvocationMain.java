package practice.phase6;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

public class SelfInvocationMain
{

	@Configuration
	@ComponentScan(basePackageClasses = SelfInvocationAspect.class)
	@EnableAspectJAutoProxy
	static class Config
	{
		@Bean
		public SelfInvocationDemo selfInvocationDemo()
		{
			return new SelfInvocationDemo();
		}
	}

	public static void main(String[] args)
	{
		var context = new AnnotationConfigApplicationContext(Config.class);

		SelfInvocationDemo demo = context.getBean(SelfInvocationDemo.class);

		System.out.println("--- 프록시 거쳐서 inner() 직접 호출 ---");
		demo.inner();

		System.out.println("--- outer()에서 내부적으로 inner() 호출 ---");
		demo.outer();

		context.close();
	}
}
