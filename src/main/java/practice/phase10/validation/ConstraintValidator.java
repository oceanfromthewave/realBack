package practice.phase10.validation;

public interface ConstraintValidator<A, T>
{
	boolean isValid(A annotation, T value);
}
