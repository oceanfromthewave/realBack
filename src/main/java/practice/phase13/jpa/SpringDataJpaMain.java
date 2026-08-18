package practice.phase13.jpa;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import practice.phase12.entity.User;
import practice.phase13.config.JpaConfig;
import practice.phase13.repository.UserRepository;

import java.util.List;
import java.util.Optional;

public class SpringDataJpaMain
{
	public static void main(String[] args)
	{
		try(AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(JpaConfig.class))
		{
			UserRepository userRepository = ctx.getBean(UserRepository.class);

			User saved = userRepository.save(new User("jaehyun", "a@b.com"));
			System.out.println("saved id= " + saved.getId());

			Optional<User> found = userRepository.findByEmail("a@b.com");
			System.out.println("found by email: " + found.orElse(null));

			List<User> byName = userRepository.findByName("jaehyun");
			System.out.println("found by name: " + byName);

			System.out.println("count: " + userRepository.count());
		}
	}
}
