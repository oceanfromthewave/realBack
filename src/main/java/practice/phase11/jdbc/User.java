package practice.phase11.jdbc;

public class User
{
	private final long id;
	private final String name;
	private final String email;

	public User(long id, String name, String email)
	{
		this.id = id;
		this.name = name;
		this.email = email;
	}

	public long getId()
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

	@Override
	public String toString()
	{
		return "User{id=" + id + ", name=" + name + ", email=" + email + "}";
	}

}
