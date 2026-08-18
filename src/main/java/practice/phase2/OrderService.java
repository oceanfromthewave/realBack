package practice.phase2;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
public class OrderService
{
	private final Notifier notifier;

	public OrderService(Notifier notifier)
	{
		this.notifier = notifier;
	}

	public void placeOrder(String item)
	{
		System.out.println("Order placed: " + item);
		notifier.send("Order placed: " + item);
	}
}

interface Notifier
{
	void send(String message);
}

@Component
class EmailNotifier implements Notifier
{
	public void send(String message)
	{
		System.out.println("[Email] " + message);
	}
}

@Component
class SmsNotifier implements Notifier
{
	public void send(String message)
	{
		System.out.println("[SMS] " + message);
	}
}
