package practice.phase10.validation;

import java.util.List;

public class MiniValidatorMain
{
	public static void main(String[] args)
	{
		MiniValidator validator = new MiniValidator();

		SignupRequest nullCase = new SignupRequest("a", null);
		System.out.println("null case: " + validator.validate(nullCase));

		SignupRequest badEmail = new SignupRequest("jaehyun", "not-an-email");
		System.out.println("bad email case: " + validator.validate(badEmail));

		SignupRequest good = new SignupRequest("jaehyun", "a@b.com");
		System.out.println("good case: " + validator.validate(good));
	}
}
