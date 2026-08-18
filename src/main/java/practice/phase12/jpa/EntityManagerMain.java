package practice.phase12.jpa;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import practice.phase12.entity.User;

public class EntityManagerMain
{
	public static void main(String[] args)
	{
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("phase12");

		// 1. INSERT
		EntityManager em1 = emf.createEntityManager();
		EntityTransaction tx1 = em1.getTransaction();

		tx1.begin();
		User user = new User("jaehyun", "a@b.com");
		em1.persist(user);
		System.out.println("after persist, id = " + user.getId());
		tx1.commit();
		em1.close();

		// 2. SELECT + Dirty Checking
		EntityManager em2 = emf.createEntityManager();
		EntityTransaction tx2 = em2.getTransaction();

		tx2.begin();
		User found = em2.find(User.class, user.getId());
		System.out.println("found: " + found);

		found.setName("changed");
		System.out.println("changed in memory, no explicit update call");
		tx2.commit();
		em2.close();

		// 3. 검증
		EntityManager em3 = emf.createEntityManager();
		User reloaded = em3.find(User.class, user.getId());
		System.out.println("reloaded: " + reloaded);
		em3.close();

		emf.close();
	}
}
