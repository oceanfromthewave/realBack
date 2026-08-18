package practice.phase8.dispatcher.annotation;

import org.apache.catalina.Context;
import org.apache.catalina.startup.Tomcat;

public class AnnotationDispatcherMain
{
	public static void main(String[] args) throws Exception
	{
		AnnotationDispatcherServlet dispatcher = new AnnotationDispatcherServlet();
		dispatcher.registerController(new HelloController());

		Tomcat tomcat = new Tomcat();
		tomcat.setPort(8080);
		tomcat.getConnector();

		Context context = tomcat.addContext("", null);
		Tomcat.addServlet(context, "annotationDispatcherServlet", dispatcher);
		context.addServletMappingDecoded("/*", "annotationDispatcherServlet");

		tomcat.start();
		tomcat.getServer().await();
	}
}
