package practice.phase9.springapp;

import org.apache.catalina.Context;
import org.apache.catalina.startup.Tomcat;
import practice.phase8.dispatcher.annotation.AnnotationDispatcherServlet;

import java.io.File;

public class MiniSpringApplication
{
	public static void run(Class<?> primarySource, Class<?>... componentClasses) throws Exception
	{
		AnnotationDispatcherServlet dispatcherServlet = new AnnotationDispatcherServlet();

		for(Class<?> componentClass : componentClasses)
		{
			if(!componentClass.isAnnotationPresent(Component.class))
			{
				throw new IllegalArgumentException(componentClass.getName() + " has no @Component");
			}

			Object bean = componentClass.getDeclaredConstructor().newInstance();
			dispatcherServlet.registerController(bean);
		}

		Tomcat tomcat = new Tomcat();
		tomcat.setPort(8080);
		tomcat.getConnector();

		String docBase = new File(".").getAbsolutePath();
		Context context = tomcat.addContext("", docBase);

		Tomcat.addServlet(context, "dispatcher", dispatcherServlet);
		context.addServletMappingDecoded("/*", "dispatcher");

		tomcat.start();

		System.out.println("MiniSpringApplication started: " + primarySource.getSimpleName());
	}
}
