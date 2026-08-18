package practice.phase13.jpa;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import practice.phase12.entity.User;
import practice.phase13.config.JpaConfig;
import practice.phase13.repository.UserRepository;

import java.util.List;

import static practice.phase13.repository.UserSpecifications.emailEndsWith;
import static practice.phase13.repository.UserSpecifications.nameContains;

public class SpecificationMain
{
	public static void main(String[] args)
	{
		try(AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(JpaConfig.class))
		{
			UserRepository userRepository = ctx.getBean(UserRepository.class);

			userRepository.save(new User("jaehyun", "a@naver.com"));
			userRepository.save(new User("jaemin", "b@gmail.com"));
			userRepository.save(new User("other", "c@naver.com"));

			List<User> result = userRepository.findAll(nameContains("jae").and(emailEndsWith("naver.com")));

			System.out.println("--- nameContains(\"jae\") AND emailEndsWith(\"naver.com\") ---");
			for(User user : result)
			{
				System.out.println(user);
			}
		}
	}
}
