package practice.phase5;

import org.springframework.stereotype.Component;

@Component
public class SmsNotifier implements Notifier
{
	public void send(String message)
	{
		System.out.println("[SMS] " + message);
	}
}
