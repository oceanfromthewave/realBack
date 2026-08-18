package practice.phase10.validation;

public class NotNullValidator implements ConstraintValidator<NotNull, Object>
{
	@Override
	public boolean isValid(NotNull annotation, Object value)
	{
		return value != null;
	}
}
