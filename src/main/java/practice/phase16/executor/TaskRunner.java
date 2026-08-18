package practice.phase16.executor;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class TaskRunner
{

	private final ExecutorService executor;

	public TaskRunner(ExecutorService executor)
	{
		this.executor = executor;
	}

	public List<String> runAll(List<Callable<String>> tasks) throws InterruptedException
	{
		List<Future<String>> futures = new ArrayList<>();
		for(Callable<String> task : tasks)
		{
			futures.add(executor.submit(task));
		}

		List<String> results = new ArrayList<>();
		for(Future<String> future : futures)
		{
			try
			{
				results.add(future.get());
			}
			catch(Exception e)
			{
				results.add("ERROR: " + e.getCause());
			}
		}

		executor.shutdown();
		executor.awaitTermination(10, TimeUnit.SECONDS);
		return results;
	}
}