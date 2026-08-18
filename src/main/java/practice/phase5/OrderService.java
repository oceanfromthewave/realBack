package practice.phase5;

import java.util.List;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class OrderService
{
	private final Notifier defaultNotifier;
	private final Notifier smsNotifier;
	private final List<Notifier> allNotifiers;

	public OrderService(Notifier defaultNotifier, @Qualifier("smsNotifier") Notifier smsNotifier, List<Notifier> allNotifiers)
	{
		this.defaultNotifier = defaultNotifier;
		this.smsNotifier = smsNotifier;
		this.allNotifiers = allNotifiers;
	}

	public void placeOrder(String item)
	{
		System.out.println("Order placed: " + item);
		defaultNotifier.send("default channel: " + item);
		smsNotifier.send("urgent channel: " + item);

		System.out.println("broadcasting to all channels (" + allNotifiers.size() + "):");
		for(Notifier n : allNotifiers)
		{
			n.send("broadcast: " + item);
		}
	}
}
