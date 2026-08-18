package practice.phase16.txasync;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Component
public class TransactionalCaller
{

	private final AsyncWorker asyncWorker;

	public TransactionalCaller(AsyncWorker asyncWorker)
	{
		this.asyncWorker = asyncWorker;
	}

	@Transactional
	public boolean runWithTransaction()
	{
		boolean activeInCallerThread = TransactionSynchronizationManager.isActualTransactionActive();
		System.out.println("[caller] thread=" + Thread.currentThread().getName() + " txActive=" + activeInCallerThread);
		asyncWorker.doWorkAsync();
		return activeInCallerThread;
	}
}
