package practice.phase16.txasync;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Component
public class AsyncWorker
{

	@Async
	public void doWorkAsync()
	{
		boolean active = TransactionSynchronizationManager.isActualTransactionActive();
		System.out.println("[async] thread=" + Thread.currentThread().getName() + " txActive=" + active);
	}
}
