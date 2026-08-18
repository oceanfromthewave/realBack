package practice.phase5;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Primary
@Component
public class EmailNotifier implements Notifier
{
	public void send(String message)
	{
		System.out.println("[Email] " + message);
	}
}
