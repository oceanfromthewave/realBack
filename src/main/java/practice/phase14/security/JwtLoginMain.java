package practice.phase14.security;

import jakarta.servlet.Filter;
import org.apache.catalina.Context;
import org.apache.catalina.startup.Tomcat;
import org.apache.tomcat.util.descriptor.web.FilterDef;
import org.apache.tomcat.util.descriptor.web.FilterMap;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
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

public class JwtLoginMain
{
	public static void main(String[] args) throws Exception
	{
		try(AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(JpaConfig.class, SecurityConfig.class))
		{
			UserRepository userRepository = ctx.getBean(UserRepository.class);
			PasswordEncoder passwordEncoder = ctx.getBean(PasswordEncoder.class);
			userRepository.save(new User("jaehyun", "a@b.com", passwordEncoder.encode("1234")));

			AuthenticationManager authenticationManager = ctx.getBean(AuthenticationManager.class);
			JwtTokenProvider tokenProvider = ctx.getBean(JwtTokenProvider.class);

			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken("a@b.com", "1234"));
			String token = tokenProvider.createToken("a@b.com");
			System.out.println("issued token: " + token);

			Filter springSecurityFilterChain = ctx.getBean("springSecurityFilterChain", Filter.class);

			Tomcat tomcat = new Tomcat();
			tomcat.setPort(8080);
			tomcat.getConnector();

			Context context = tomcat.addContext("", null);
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
			call(client, token);
			call(client, "not.a.valid.token");
			call(client, null);

			tomcat.stop();
		}
	}

	private static void call(HttpClient client, String token) throws Exception
	{
		HttpRequest.Builder builder = HttpRequest.newBuilder(URI.create("http://localhost:8080/hello")).GET();
		if(token != null)
		{
			builder.header("Authorization", "Bearer " + token);
		}

		HttpResponse<String> response = client.send(builder.build(), HttpResponse.BodyHandlers.ofString());
		System.out.println("token=" + token + " -> " + response.statusCode());
	}
}
