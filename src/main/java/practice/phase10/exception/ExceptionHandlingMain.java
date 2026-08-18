package practice.phase10.exception;

import org.apache.catalina.Context;
import org.apache.catalina.startup.Tomcat;

import java.io.File;

public class ExceptionHandlingMain
{
	public static void main(String[] args) throws Exception
	{
		ExceptionHandlingDispatcherServlet dispatcherServlet = new ExceptionHandlingDispatcherServlet();
		dispatcherServlet.registerController(new SignupController());
		dispatcherServlet.registerControllerAdvice(new GlobalExceptionHandler());

		Tomcat tomcat = new Tomcat();
		tomcat.setPort(8080);
		tomcat.getConnector();

		String docBase = new File(".").getAbsolutePath();
		Context context = tomcat.addContext("", docBase);

		Tomcat.addServlet(context, "dispatcher", dispatcherServlet);
		context.addServletMappingDecoded("/*", "dispatcher");

		tomcat.start();

		System.out.println("ExceptionHandlingMain started");
	}
}
