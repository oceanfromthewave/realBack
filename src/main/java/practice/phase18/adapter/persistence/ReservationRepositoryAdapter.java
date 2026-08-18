package practice.phase18.adapter.persistence;

import org.springframework.stereotype.Repository;
import practice.phase18.domain.Reservation;
import practice.phase18.port.ReservationRepository;

import java.util.Optional;

@Repository
public class ReservationRepositoryAdapter implements ReservationRepository
{

	private final ReservationJpaRepository jpaRepository;

	public ReservationRepositoryAdapter(ReservationJpaRepository jpaRepository)
	{
		this.jpaRepository = jpaRepository;
	}

	@Override
	public Reservation save(Reservation reservation)
	{
		ReservationJpaEntity entity = toEntity(reservation);
		jpaRepository.save(entity);
		return reservation;
	}

	@Override
	public Optional<Reservation> findById(String id)
	{
		return jpaRepository.findById(id).map(this::toDomain);
	}

	private ReservationJpaEntity toEntity(Reservation r)
	{
		return new ReservationJpaEntity(r.getId(), r.getProductId(), r.getQuantity(), r.getExpiresAt(), r.getStatus().name());
	}

	private Reservation toDomain(ReservationJpaEntity e)
	{
		Reservation r = Reservation.reconstitute(e.getId(), e.getProductId(), e.getQuantity(), e.getExpiresAt(), Reservation.Status.valueOf(e.getStatus()));
		return r;
	}
}
