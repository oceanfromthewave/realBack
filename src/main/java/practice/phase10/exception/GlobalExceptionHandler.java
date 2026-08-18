package practice.phase10.exception;

@ControllerAdvice
public class GlobalExceptionHandler
{
	@ExceptionHandler(ValidationException.class)
	public ErrorResponse handleValidation(ValidationException e)
	{
		return new ErrorResponse(400, e.getMessage());
	}
}
