package practice.phase3;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan
class AppConfig
{
}

public class Main
{
	public static void main(String[] args)
	{
		var context = new AnnotationConfigApplicationContext(AppConfig.class);
		LifecycleDemoBean bean = context.getBean(LifecycleDemoBean.class);
		System.out.println("bean ready: " + bean);
		context.close();
	}
}
