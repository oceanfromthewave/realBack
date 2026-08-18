package practice.phase10.validation;

public class SignupRequest
{
	@NotNull(message = "name must not be null")
	@Size(min = 2, max = 20, message = "name must be 2~20 chars")
	private String name;

	@NotNull(message = "email must not be null")
	@Email(message = "email format invalid")
	private String email;

	public SignupRequest(String name, String email)
	{
		this.name = name;
		this.email = email;
	}

	public String getName()
	{
		return name;
	}

	public String getEmail()
	{
		return email;
	}
}
