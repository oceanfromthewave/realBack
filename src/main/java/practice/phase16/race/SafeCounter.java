package practice.phase16.race;

import java.util.concurrent.atomic.AtomicInteger;

public class SafeCounter
{

	private int syncCount = 0;
	private final AtomicInteger atomicCount = new AtomicInteger(0);

	public synchronized void incrementWithLock()
	{
		syncCount++; // 진입할 때 monitor lock 획득, 끝나면 해제
	}

	public synchronized int getSyncCount()
	{
		return syncCount;
	}

	public void incrementWithAtomic()
	{
		atomicCount.incrementAndGet(); // CAS 기반, lock 없이 원자적
	}

	public int getAtomicCount()
	{
		return atomicCount.get();
	}
}
