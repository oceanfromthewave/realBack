package practice.phase10.exception;

import practice.phase10.validation.ConstraintViolation;

import java.util.List;

public class ValidationException extends RuntimeException
{
	private final List<ConstraintViolation> violations;

	public ValidationException(List<ConstraintViolation> violations)
	{
		super("validation failed: " + violations);
		this.violations = violations;
	}

	public List<ConstraintViolation> getViolations()
	{
		return violations;
	}
}
