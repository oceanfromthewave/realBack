package practice.phase14.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import practice.phase13.repository.UserRepository;
import practice.phase14.security.CustomUserDetailsService;
import practice.phase14.security.JwtAuthenticationFilter;
import practice.phase14.security.JwtTokenProvider;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig
{
	@Bean
	public PasswordEncoder passwordEncoder()
	{
		return new BCryptPasswordEncoder();
	}

	@Bean
	public CustomUserDetailsService userDetailsService(UserRepository userRepository)
	{
		return new CustomUserDetailsService(userRepository);
	}

	@Bean
	public DaoAuthenticationProvider authenticationProvider(CustomUserDetailsService userDetailsService, PasswordEncoder passwordEncoder)
	{
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
		provider.setUserDetailsService(userDetailsService);
		provider.setPasswordEncoder(passwordEncoder);
		return provider;
	}

	@Bean
	public AuthenticationManager authenticationManager(DaoAuthenticationProvider provider)
	{
		return new ProviderManager(List.of(provider));
	}

	@Bean
	public JwtTokenProvider jwtTokenProvider()
	{
		return new JwtTokenProvider("phase14-jwt-secret-key-please-be-32-bytes-min", 1000 * 60 * 30);
	}

	@Bean
	public AuthenticationEntryPoint authenticationEntryPoint()
	{
		return (request, response, authException) -> {
			response.setStatus(401);
			response.setContentType("application/json;charset=UTF-8");
			response.getWriter().write("{\"error\":\"unauthorized\"}");
		};
	}

	@Bean
	public CorsConfigurationSource corsConfigurationSource()
	{
		CorsConfiguration config = new CorsConfiguration();
		config.setAllowedOrigins(List.of("http://localhost:3000"));
		config.setAllowedMethods(List.of("GET", "POST"));
		config.setAllowedHeaders(List.of("Authorization", "Content-Type"));

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", config);
		return source;
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http, AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider,
			CustomUserDetailsService userDetailsService, AuthenticationEntryPoint authenticationEntryPoint,
			CorsConfigurationSource corsConfigurationSource) throws Exception
	{
		http.authenticationManager(authenticationManager).sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.cors(cors -> cors.configurationSource(corsConfigurationSource))
				.authorizeHttpRequests(auth -> auth.requestMatchers("/public", "/login").permitAll().anyRequest().authenticated())
				.addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider, userDetailsService), UsernamePasswordAuthenticationFilter.class)
				.exceptionHandling(ex -> ex.authenticationEntryPoint(authenticationEntryPoint)).csrf(csrf -> csrf.disable());

		return http.build();
	}

}
