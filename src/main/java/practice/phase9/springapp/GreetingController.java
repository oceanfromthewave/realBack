package practice.phase9.springapp;

import practice.phase8.dispatcher.annotation.GetMapping;
import practice.phase8.dispatcher.annotation.RequestParam;

@Component
public class GreetingController
{
	@GetMapping("/hi")
	public String hi(@RequestParam("name") String name)
	{
		return "hi, " + name + " from MiniSpringApplication";
	}
}
