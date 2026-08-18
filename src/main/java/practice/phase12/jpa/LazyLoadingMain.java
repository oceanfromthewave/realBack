package practice.phase12.jpa;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import practice.phase12.entity.Order;
import practice.phase12.entity.User;

import java.util.List;

public class LazyLoadingMain
{
	public static void main(String[] args)
	{
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("phase12");

		// 데이터 준비: user 2명, order 3개 (user1이 2개, user2가 1개)
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

		// Lazy Proxy 확인 + N+1 재현
		EntityManager em2 = emf.createEntityManager();
		EntityTransaction tx2 = em2.getTransaction();

		tx2.begin();
		List<Order> orders = em2.createQuery("select o from Order o", Order.class).getResultList();
		System.out.println("--- orders 조회 끝, user는 아직 안 건드림 ---");

		System.out.println("proxy class: " + orders.get(0).getUser().getClass());

		System.out.println("--- 이제 각 order의 user.getName() 접근 시작 ---");
		for(Order order : orders)
		{
			System.out.println(order.getProduct() + " -> " + order.getUser().getName());
		}
		tx2.commit();
		em2.close();

		emf.close();
	}
}
