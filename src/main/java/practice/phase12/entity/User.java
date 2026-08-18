package practice.phase12.entity;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
public class User
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String name;

	@Column(nullable = false, unique = true)
	private String email;

	@Column
	private String password;

	@OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
	private List<Order> orders = new ArrayList<>();

	protected User()
	{

	}

	public User(String name, String email, String password)
	{
		this.name = name;
		this.email = email;
		this.password = password;
	}

	public User(String name, String email)
	{
		this(name, email, null);
	}

	public Long getId()
	{
		return id;
	}

	public String getName()
	{
		return name;
	}

	public String getEmail()
	{
		return email;
	}

	public String getPassword()
	{
		return password;
	}

	public List<Order> getOrders()
	{
		return orders;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	@Override
	public String toString()
	{
		return "User{id=" + id + ", name=" + name + ", email=" + email + "}";
	}
}
