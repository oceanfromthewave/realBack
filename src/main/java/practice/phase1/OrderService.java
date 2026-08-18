package practice.phase1;

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
