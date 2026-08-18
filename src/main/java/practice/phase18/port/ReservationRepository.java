package practice.phase18.port;

import practice.phase18.domain.Reservation;

import java.util.Optional;

public interface ReservationRepository
{
	Reservation save(Reservation reservation);

	Optional<Reservation> findById(String id);
}
