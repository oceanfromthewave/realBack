package practice.phase18.domain;

import java.time.Instant;
import java.util.UUID;

public class Reservation
{

	public enum Status
	{PENDING, CONFIRMED, CANCELLED, EXPIRED}

	private final String id;
	private final String productId;
	private final int quantity;
	private final Instant expiresAt;
	private Status status;

	public Reservation(String productId, int quantity, Instant expiresAt)
	{
		if(quantity <= 0)
		{
			throw new IllegalArgumentException("quantity must be positive: " + quantity);
		}
		this.id = UUID.randomUUID().toString();
		this.productId = productId;
		this.quantity = quantity;
		this.expiresAt = expiresAt;
		this.status = Status.PENDING;
	}

	public void confirm()
	{
		if(status != Status.PENDING)
		{
			throw new IllegalStateException("cannot confirm from status: " + status);
		}
		this.status = Status.CONFIRMED;
	}

	public void cancel()
	{
		if(status != Status.PENDING && status != Status.CONFIRMED)
		{
			throw new IllegalStateException("cannot cancel from status: " + status);
		}
		this.status = Status.CANCELLED;
	}

	public boolean isExpired(Instant now)
	{
		return status == Status.PENDING && now.isAfter(expiresAt);
	}

	public void expire(Instant now)
	{
		if(!isExpired(now))
		{
			throw new IllegalStateException("not expired yet, or not PENDING (status=" + status + ")");
		}
		this.status = Status.EXPIRED;
	}

	public String getId()
	{
		return id;
	}

	public String getProductId()
	{
		return productId;
	}

	public int getQuantity()
	{
		return quantity;
	}

	public Instant getExpiresAt()
	{
		return expiresAt;
	}

	public Status getStatus()
	{
		return status;
	}

	public static Reservation reconstitute(String id, String productId, int quantity, Instant expiresAt, Status status)
	{
		Reservation r = new Reservation(id, productId, quantity, expiresAt, status);
		return r;
	}

	private Reservation(String id, String productId, int quantity, Instant expiresAt, Status status)
	{
		this.id = id;
		this.productId = productId;
		this.quantity = quantity;
		this.expiresAt = expiresAt;
		this.status = status;
	}
}
