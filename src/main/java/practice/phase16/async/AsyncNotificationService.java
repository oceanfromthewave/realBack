package practice.phase16.async;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class AsyncNotificationService
{
	@Async("asyncExecutor")
	public CompletableFuture<String> send(String messase)
	{
		String threadName = Thread.currentThread().getName();
		try
		{
			Thread.sleep(500);
		}
		catch(InterruptedException e)
		{
			Thread.currentThread().interrupt();
		}
		return CompletableFuture.completedFuture(messase + " sent by " + threadName);
	}
}
