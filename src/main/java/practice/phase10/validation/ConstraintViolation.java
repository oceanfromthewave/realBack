package practice.phase10.validation;

public class ConstraintViolation
{
	private final String field;
	private final String message;

	public ConstraintViolation(String field, String message)
	{
		this.field = field;
		this.message = message;
	}

	public String getField()
	{
		return field;
	}

	public String getMessage()
	{
		return message;
	}

	@Override
	public String toString()
	{
		return field + ": " + message;
	}
}
