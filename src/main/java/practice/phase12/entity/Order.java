package practice.phase12.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "orders")
public class Order
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String product;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;

	protected Order()
	{
	}

	public Order(String product, User user)
	{
		this.product = product;
		this.user = user;
	}

	public Long getId()
	{
		return id;
	}

	public String getProduct()
	{
		return product;
	}

	public User getUser()
	{
		return user;
	}

	@Override
	public String toString()
	{
		return "Order{id=" + id + ", product=" + product + "}";
	}
}
