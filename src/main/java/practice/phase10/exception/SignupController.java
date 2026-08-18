package practice.phase10.exception;

import practice.phase10.validation.ConstraintViolation;
import practice.phase10.validation.MiniValidator;
import practice.phase10.validation.SignupRequest;
import practice.phase8.dispatcher.annotation.GetMapping;
import practice.phase8.dispatcher.annotation.RequestParam;

import java.util.List;

public class SignupController
{
	private final MiniValidator validator = new MiniValidator();

	@GetMapping("/signup")
	public SignupRequest signup(@RequestParam("name") String name, @RequestParam("email") String email)
	{
		SignupRequest request = new SignupRequest(name, email);
		List<ConstraintViolation> violations = validator.validate(request);

		if(!violations.isEmpty())
		{
			throw new ValidationException(violations);
		}
		return request;
	}
}
