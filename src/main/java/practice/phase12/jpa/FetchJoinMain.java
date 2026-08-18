package practice.phase12.jpa;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import practice.phase12.entity.Order;
import practice.phase12.entity.User;

import java.util.List;

public class FetchJoinMain
{
	public static void main(String[] args)
	{
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("phase12");

		EntityManager em1 = emf.createEntityManager();
		EntityTransaction tx1 = em1.getTransaction();
		tx1.begin();
		User user1 = new User("jaehyun", "a@b.com");
		User user2 = new User("other", "c@d.com");
		em1.persist(user1);
		em1.persist(user2);
		em1.persist(new Order("keyboard", user1));
		em1.persist(new Order("mouse", user1));
		em1.persist(new Order("monitor", user2));
		tx1.commit();
		em1.close();

		EntityManager em2 = emf.createEntityManager();
		List<Order> orders = em2.createQuery(
						"select o from Order o join fetch o.user", Order.class)
				.getResultList();

		System.out.println("--- 쿼리 끝, 이제 user 접근 ---");
		for (Order order : orders)
		{
			System.out.println(order.getProduct() + " -> " + order.getUser().getName());
		}

		em2.close();
		emf.close();
	}
}

