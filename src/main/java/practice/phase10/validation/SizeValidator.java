package practice.phase10.validation;

public class SizeValidator implements ConstraintValidator<Size, String>
{
	@Override
	public boolean isValid(Size annotation, String value)
	{
		if(value == null)
		{
			return true;
		}
		return value.length() >= annotation.min() && value.length() <= annotation.max();
	}
}
