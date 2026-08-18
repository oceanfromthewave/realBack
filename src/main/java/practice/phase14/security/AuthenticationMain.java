package practice.phase14.security;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import practice.phase12.entity.User;
import practice.phase13.config.JpaConfig;
import practice.phase13.repository.UserRepository;
import practice.phase14.config.SecurityConfig;

public class AuthenticationMain
{
	public static void main(String[] args)
	{
		try (AnnotationConfigApplicationContext ctx =
				new AnnotationConfigApplicationContext(JpaConfig.class, SecurityConfig.class))
		{
			UserRepository userRepository = ctx.getBean(UserRepository.class);
			PasswordEncoder passwordEncoder = ctx.getBean(PasswordEncoder.class);
			AuthenticationManager authenticationManager = ctx.getBean(AuthenticationManager.class);

			String rawPassword = "1234";
			userRepository.save(new User("jaehyun", "a@b.com", passwordEncoder.encode(rawPassword)));

			Authentication success = authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken("a@b.com", rawPassword));
			System.out.println("success: " + success.isAuthenticated() + ", authorities: " + success.getAuthorities());

			try
			{
				authenticationManager.authenticate(
						new UsernamePasswordAuthenticationToken("a@b.com", "wrong"));
			}
			catch (BadCredentialsException e)
			{
				System.out.println("failed as expected: " + e.getMessage());
			}
		}
	}
}
