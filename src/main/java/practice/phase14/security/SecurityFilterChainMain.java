package practice.phase14.security;

import jakarta.servlet.Filter;
import org.apache.catalina.Context;
import org.apache.catalina.startup.Tomcat;
import org.apache.tomcat.util.descriptor.web.FilterDef;
import org.apache.tomcat.util.descriptor.web.FilterMap;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.security.crypto.password.PasswordEncoder;
import practice.phase12.entity.User;
import practice.phase13.config.JpaConfig;
import practice.phase13.repository.UserRepository;
import practice.phase14.config.SecurityConfig;
import practice.phase8.servlet.HelloServlet;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Base64;

public class SecurityFilterChainMain
{
	public static void main(String[] args) throws Exception
	{
		try(AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(JpaConfig.class, SecurityConfig.class))
		{
			UserRepository userRepository = ctx.getBean(UserRepository.class);
			PasswordEncoder passwordEncoder = ctx.getBean(PasswordEncoder.class);
			userRepository.save(new User("jaehyun", "a@b.com", passwordEncoder.encode("1234")));

			Filter springSecurityFilterChain = ctx.getBean("springSecurityFilterChain", Filter.class);

			Tomcat tomcat = new Tomcat();
			tomcat.setPort(8080);
			tomcat.getConnector();

			Context context = tomcat.addContext("", null);
			Tomcat.addServlet(context, "publicServlet", new HelloServlet());
			context.addServletMappingDecoded("/public", "publicServlet");
			Tomcat.addServlet(context, "helloServlet", new HelloServlet());
			context.addServletMappingDecoded("/hello", "helloServlet");

			FilterDef filterDef = new FilterDef();
			filterDef.setFilterName("springSecurityFilterChain");
			filterDef.setFilter(springSecurityFilterChain);
			context.addFilterDef(filterDef);

			FilterMap filterMap = new FilterMap();
			filterMap.setFilterName("springSecurityFilterChain");
			filterMap.addURLPattern("/*");
			context.addFilterMap(filterMap);

			tomcat.start();

			HttpClient client = HttpClient.newHttpClient();
			call(client, "/public", null);
			call(client, "/hello", null);
			call(client, "/hello", "a@b.com:wrong");
			call(client, "/hello", "a@b.com:1234");

			tomcat.stop();
		}
	}

	private static void call(HttpClient client, String path, String basicCredentials) throws Exception
	{
		HttpRequest.Builder builder = HttpRequest.newBuilder(URI.create("http://localhost:8080" + path)).GET();
		if(basicCredentials != null)
		{
			String encoded = Base64.getEncoder().encodeToString(basicCredentials.getBytes());
			builder.header("Authorization", "Basic " + encoded);
		}

		HttpResponse<String> response = client.send(builder.build(), HttpResponse.BodyHandlers.ofString());
		System.out.println(path + " (" + basicCredentials + ") -> " + response.statusCode() + " " + response.body().trim());
	}
}
