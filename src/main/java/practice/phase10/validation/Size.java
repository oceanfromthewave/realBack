package practice.phase10.validation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Constraint(validatedBy = SizeValidator.class)
public @interface Size
{
	int min() default 0;

	int max() default Integer.MAX_VALUE;

	String message() default "size out of range";
}
