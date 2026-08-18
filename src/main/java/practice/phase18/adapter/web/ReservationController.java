package practice.phase18.adapter.web;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import practice.phase18.domain.Reservation;
import practice.phase18.usecase.ReserveStockUseCase;

import java.time.Duration;
import java.time.Instant;

@RestController
@RequestMapping("/reservations")
public class ReservationController
{

	private final ReserveStockUseCase reserveStockUseCase;

	public ReservationController(ReserveStockUseCase reserveStockUseCase)
	{
		this.reserveStockUseCase = reserveStockUseCase;
	}

	@PostMapping
	public ReserveResponse reserve(@RequestBody ReserveRequest request)
	{
		Reservation reservation = reserveStockUseCase.reserve(request.productId(), request.quantity(), Duration.ofSeconds(request.ttlSeconds()));
		return ReserveResponse.from(reservation);
	}

	public record ReserveRequest(String productId, int quantity, long ttlSeconds)
	{
	}

	public record ReserveResponse(String id, String productId, int quantity, String status, Instant expiresAt)
	{
		static ReserveResponse from(Reservation r)
		{
			return new ReserveResponse(r.getId(), r.getProductId(), r.getQuantity(), r.getStatus().name(), r.getExpiresAt());
		}
	}
}
