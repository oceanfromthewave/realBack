package practice.phase18.adapter.persistence;

import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(name = "reservation")
public class ReservationJpaEntity
{
	@Id
	private String id;

	private String productId;

	private int quantity;

	private Instant expiresAt;

	private String status;

	protected ReservationJpaEntity()
	{

	}

	public ReservationJpaEntity(String id, String productId, int quantity, Instant expiresAt, String status)
	{
		this.id = id;
		this.productId = productId;
		this.quantity = quantity;
		this.expiresAt = expiresAt;
		this.status = status;
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

	public String getStatus()
	{
		return status;
	}
}
