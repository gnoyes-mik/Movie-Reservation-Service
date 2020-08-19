package kr.co.leadsoft.mrmserver.entity;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationRequestLogRepository extends JpaRepository<ReservationRequestLogEntity, Long> {

    ReservationRequestLogEntity findByTheaterNumberAndDateIsBetweenAndSeat(int theaterNumber, String start, String end, int seat);
}
