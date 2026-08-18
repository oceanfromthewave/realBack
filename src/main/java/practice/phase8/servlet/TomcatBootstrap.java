package practice.phase8.servlet;

import org.apache.catalina.Context;
import org.apache.catalina.startup.Tomcat;
import org.apache.tomcat.util.descriptor.web.FilterDef;
import org.apache.tomcat.util.descriptor.web.FilterMap;

public class TomcatBootstrap
{
	public static void main(String[] args) throws Exception
	{
		Tomcat tomcat = new Tomcat();
		tomcat.setPort(8080);
		tomcat.getConnector();

		Context context = tomcat.addContext("", null);
		Tomcat.addServlet(context, "helloServlet", new HelloServlet());
		context.addServletMappingDecoded("/hello", "helloServlet");

		FilterDef filterDef = new FilterDef();
		filterDef.setFilterName("loggingFilter");
		filterDef.setFilter(new LoggingFilter());
		context.addFilterDef(filterDef);

		FilterMap filterMap = new FilterMap();
		filterMap.setFilterName("loggingFilter");
		filterMap.addURLPattern("/*");
		context.addFilterMap(filterMap);

		tomcat.start();
		tomcat.getServer().await();
	}
}
