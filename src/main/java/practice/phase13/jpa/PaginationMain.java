package practice.phase13.jpa;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import practice.phase12.entity.User;
import practice.phase13.config.JpaConfig;
import practice.phase13.repository.UserRepository;

public class PaginationMain
{
	public static void main(String[] args)
	{
		try(AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(JpaConfig.class))
		{
			UserRepository userRepository = ctx.getBean(UserRepository.class);

			for(int i = 1; i <= 5; i++)
			{
				userRepository.save(new User("user" + i, "user" + i + "@b.com"));
			}

			Page<User> page = userRepository.findAll(PageRequest.of(0, 2, Sort.by("name").descending()));

			System.out.println("totalElements = " + page.getTotalElements());
			System.out.println("totalPages = " + page.getTotalPages());
			for(User user : page.getContent())
			{
				System.out.println(user);
			}
		}
	}
}
