package practice.phase13.jpa;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import practice.phase12.entity.Order;
import practice.phase12.entity.User;
import practice.phase13.config.JpaConfig;
import practice.phase13.repository.OrderRepository;
import practice.phase13.repository.UserRepository;

import java.util.List;

public class OrderRepositoryMain
{
	public static void main(String[] args)
	{
		try(AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(JpaConfig.class))
		{
			UserRepository userRepository = ctx.getBean(UserRepository.class);
			OrderRepository orderRepository = ctx.getBean(OrderRepository.class);

			User user = userRepository.save(new User("jaehyun", "a@b.com"));
			orderRepository.save(new Order("keyboard", user));
			orderRepository.save(new Order("mouse", user));

			System.out.println("--- findAllWithUser (fetch join) ---");
			List<Order> withUser = orderRepository.findAllWithUser();
			for(Order order : withUser)
			{
				System.out.println(order.getProduct() + " -> " + order.getUser().getName());
			}

			System.out.println("--- findByProductNative ---");
			List<Order> byProduct = orderRepository.findByProductNative("keyboard");
			System.out.println(byProduct);
		}
	}
}
