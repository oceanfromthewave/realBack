package practice.phase1;

public class LoggingNotifier implements Notifier
{
	private final Notifier target;

	public LoggingNotifier(Notifier target)
	{
		this.target = target;
	}

	public void send(String message)
	{
		System.out.println("[LOG] before send: " + message);
		target.send(message);
		System.out.println("[LOG] after send " + message);
	}

}
