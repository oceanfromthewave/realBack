package practice.phase10.validation;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class MiniValidator
{
	public List<ConstraintViolation> validate(Object target)
	{
		List<ConstraintViolation> violations = new ArrayList<>();

		for(Field field : target.getClass().getDeclaredFields())
		{
			field.setAccessible(true);
			Object value = readValue(field, target);

			for(Annotation annotation : field.getAnnotations())
			{
				Constraint constraint = annotation.annotationType().getAnnotation(Constraint.class);
				if(constraint == null)
				{
					continue;
				}

				if(!isValid(constraint.validatedBy(), annotation, value))
				{
					violations.add(new ConstraintViolation(field.getName(), readMessage(annotation)));
				}
			}
		}

		return violations;
	}

	@SuppressWarnings("unchecked")
	private boolean isValid(Class<? extends ConstraintValidator<?, ?>> validatorClass, Annotation annotation, Object value)
	{
		try
		{
			ConstraintValidator<Annotation, Object> validator = (ConstraintValidator<Annotation, Object>) validatorClass.getDeclaredConstructor().newInstance();
			return validator.isValid(annotation, value);
		}
		catch(Exception e)
		{
			throw new RuntimeException(e);
		}
	}

	private String readMessage(Annotation annotation)
	{
		try
		{
			Method messageMethod = annotation.annotationType().getMethod("message");
			return (String) messageMethod.invoke(annotation);
		}
		catch(Exception e)
		{
			throw new RuntimeException(e);
		}
	}

	private Object readValue(Field field, Object target)
	{
		try
		{
			return field.get(target);
		}
		catch(IllegalAccessException e)
		{
			throw new RuntimeException(e);
		}
	}
}
