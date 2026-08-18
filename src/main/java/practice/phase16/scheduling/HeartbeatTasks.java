package practice.phase16.scheduling;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class HeartbeatTasks
{

	@Scheduled(fixedRate = 1000)
	public void fastTask()
	{
		System.out.println("[fast] " + System.currentTimeMillis() + " on " + Thread.currentThread().getName());
	}

	@Scheduled(fixedRate = 1000)
	public void slowTask()
	{
		System.out.println("[slow] start " + System.currentTimeMillis() + " on " + Thread.currentThread().getName());
		try
		{
			Thread.sleep(3000); // 일부러 느린 작업
		}
		catch(InterruptedException e)
		{
			Thread.currentThread().interrupt();
		}
		System.out.println("[slow] end " + System.currentTimeMillis());
	}
}
