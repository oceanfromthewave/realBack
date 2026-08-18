package practice.phase8.dispatcher;

import org.apache.catalina.Context;
import org.apache.catalina.startup.Tomcat;

public class DispatcherMain
{
	public static void main(String[] args) throws Exception
	{
		MiniDispatcherServlet dispatcher = new MiniDispatcherServlet();

		dispatcher.registerHandler("/hello", (req, resp) -> {
			resp.setContentType("text/plain;charset=UTF-8");
			resp.getWriter().println("hello from dispatcher");
		});

		dispatcher.registerHandler("/bye", (req, resp) -> {
			resp.setContentType("text/plain;charset=UTF-8");
			resp.getWriter().println("bye from dispatcher");
		});

		Tomcat tomcat = new Tomcat();
		tomcat.setPort(8080);
		tomcat.getConnector();

		Context context = tomcat.addContext("", null);
		Tomcat.addServlet(context, "dispatcherServlet", dispatcher);
		context.addServletMappingDecoded("/*", "dispatcherServlet");

		tomcat.start();
		tomcat.getServer().await();
	}
}
