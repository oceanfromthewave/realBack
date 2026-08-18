package practice.phase18.usecase;

import practice.phase18.domain.Reservation;
import practice.phase18.port.ReservationRepository;

import java.time.Duration;
import java.time.Instant;

public class ReserveStockUseCase
{
	private final ReservationRepository reservationRepository;

	public ReserveStockUseCase(ReservationRepository reservationRepository)
	{
		this.reservationRepository = reservationRepository;
	}

	public Reservation reserve(String productId, int quantity, Duration ttl)
	{
		Reservation reservation = new Reservation(productId, quantity, Instant.now().plus(ttl));
		return reservationRepository.save(reservation);
	}
}
