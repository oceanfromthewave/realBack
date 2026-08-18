package practice.phase13.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import practice.phase12.entity.Order;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long>
{
	@Query("select o from Order o join fetch o.user")
	List<Order> findAllWithUser();

	@Query(value = "select * from orders where product = ?1", nativeQuery = true)
	List<Order> findByProductNative(String product);
}
