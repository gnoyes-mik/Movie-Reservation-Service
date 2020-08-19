package kr.co.leadsoft.smserver.entity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface SeatReservationRepository extends JpaRepository<SeatReservationEntity, Integer> {
    List<SeatReservationEntity> findByTheaterNumberAndDateIsBetween(int theaterNumber, String start, String end);
}
