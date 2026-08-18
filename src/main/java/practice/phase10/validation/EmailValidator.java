package practice.phase10.validation;

public class EmailValidator implements ConstraintValidator<Email, String>
{
	@Override
	public boolean isValid(Email annotation, String value)
	{
		if(value == null)
		{
			return true;
		}
		return value.matches("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$");
	}
}
