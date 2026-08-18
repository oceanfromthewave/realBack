package practice.phase17.resilience;

import java.time.Instant;

public class CircuitBreaker
{
	public enum State
	{CLOSED, OPEN, HALF_OPEN}

	private final int failureThreshold;
	private final long resetTimeoutMillis;

	private State state = State.CLOSED;
	private int failureCount = 0;
	private Instant openedAt;

	public CircuitBreaker(int failureThreshold, long resetTimeoutMillis)
	{
		this.failureThreshold = failureThreshold;
		this.resetTimeoutMillis = resetTimeoutMillis;
	}

	public synchronized boolean allowRequest()
	{
		if(state == State.OPEN)
		{
			if(Instant.now().isAfter(openedAt.plusMillis(resetTimeoutMillis)))
			{
				state = State.HALF_OPEN;
				return true;
			}
			return false;
		}
		return true;
	}

	public synchronized void recordSuccess()
	{
		failureCount = 0;
		state = State.CLOSED;
	}

	public synchronized void recordFailure()
	{
		failureCount++;
		if(state == State.HALF_OPEN || failureCount >= failureThreshold)
		{
			state = State.OPEN;
			openedAt = Instant.now();
		}
	}

	public synchronized State getState()
	{
		return state;
	}
}
