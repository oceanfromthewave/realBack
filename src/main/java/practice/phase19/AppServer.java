package practice.phase19;

import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;
import practice.phase18.config.AppConfig;
import practice.phase18.config.WebConfig;

public class AppServer
{
	private static final Logger log = LoggerFactory.getLogger(AppServer.class);

	public static void main(String[] args) throws LifecycleException
	{
		int port = Integer.parseInt(System.getenv().getOrDefault("APP_PORT", "8080"));

		AnnotationConfigWebApplicationContext ctx = new AnnotationConfigWebApplicationContext();
		ctx.register(AppConfig.class, WebConfig.class);

		Tomcat tomcat = new Tomcat();
		tomcat.setPort(port);
		tomcat.getConnector();

		Context context = tomcat.addContext("", null);
		DispatcherServlet dispatcherServlet = new DispatcherServlet(ctx);
		Tomcat.addServlet(context, "dispatcher", dispatcherServlet).setLoadOnStartup(1);
		context.addServletMappingDecoded("/*", "dispatcher");

		tomcat.start();
		log.info("AppServer started on port {}", port);

		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			log.info("Shutting down...");
			try
			{
				tomcat.stop();
			}
			catch(LifecycleException e)
			{
				throw new RuntimeException(e);
			}
			ctx.close();
			log.info("Shutdown complete");
		}));

		tomcat.getServer().await();
	}
}
