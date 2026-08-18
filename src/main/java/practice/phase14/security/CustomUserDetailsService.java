package practice.phase14.security;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import practice.phase13.repository.UserRepository;

public class CustomUserDetailsService implements UserDetailsService
{
	private final UserRepository userRepository;

	public CustomUserDetailsService(UserRepository userRepository)
	{
		this.userRepository = userRepository;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException
	{
		practice.phase12.entity.User user = userRepository.findByEmail(username)
				.orElseThrow(() -> new UsernameNotFoundException("no user: " + username));

		return User.withUsername(user.getEmail())
				.password(user.getPassword())
				.roles("USER")
				.build();
	}
}