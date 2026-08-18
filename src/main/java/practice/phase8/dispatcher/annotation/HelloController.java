package practice.phase8.dispatcher.annotation;

public class HelloController
{
	@GetMapping("/hello")
	public String hello()
	{
		return "hello from annotation controller";
	}

	@GetMapping("/bye")
	public String bye()
	{
		return "bye from annotation controller";
	}

	@GetMapping("/greet")
	public String greet(@RequestParam("name") String name)
	{
		return "greet, " + name;
	}

	@GetMapping("/user")
	public User user(@RequestParam("name") String name)
	{
		return new User(name, 30);
	}
}
