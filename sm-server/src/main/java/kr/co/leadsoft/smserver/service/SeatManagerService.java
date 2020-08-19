package kr.co.leadsoft.smserver.service;

import kr.co.leadsoft.smserver.entity.SeatReservationEntity;
import kr.co.leadsoft.smserver.entity.SeatReservationRepository;
import kr.co.leadsoft.smserver.model.ReservationRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class SeatManagerService {

    private final SeatReservationRepository seatReservationRepository;

    /**
     * 상영관 예약 현황 확인 (reserved_seat)
     *
     * @param theaterNumber Integer
     *                      상영관 번호
     * @return HashMap
     */
    public HashMap<String, Object> getReservationStatus(int theaterNumber) {
        HashMap<String, Object> theaterStatus = new HashMap<>();

        List<SeatReservationEntity> reservations = fetchReservationInfo(theaterNumber);

        theaterStatus.put("reserved_seat", reservations.size());

        if (reservations.size() > 0) {
            theaterStatus.put("reserved_seat", reservations.size());
            log.info(theaterStatus.toString());
            return theaterStatus;
        }
        log.info(theaterStatus.toString());
        return theaterStatus;
    }

    /**
     * 상영관 과거 예약 현황 확인 (reserved_seat, reservationList)
     *
     * @param theaterNumber Integer
     *                      상영관 번호
     * @param startTime     String
     *                      영화 시작 시간
     * @param endTime       String
     *                      영화 종료 시간
     * @return HashMap
     */
    public HashMap<String, Object> getReservationStatus(int theaterNumber, String startTime, String endTime) {
        HashMap<String, Object> theaterStatus = new HashMap<>();

        List<SeatReservationEntity> reservations = fetchReservationInfo(theaterNumber, startTime, endTime);

        theaterStatus.put("reserved_seat", reservations.size());

        if (reservations.size() > 0) {
            theaterStatus.put("reserved_seat", reservations.size());
            theaterStatus.put("reservations", reservations);
            log.info(theaterStatus.toString());
            return theaterStatus;
        }
        log.info(theaterStatus.toString());
        return theaterStatus;
    }


    /**
     * DB에서 특정 상영관의 현재 예약 현황 조회
     *
     * @param theaterNumber Integer
     *                      상영관 번호
     * @return 예약 목록
     */
    private List<SeatReservationEntity> fetchReservationInfo(int theaterNumber) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:00:00");
        Calendar currentTime = Calendar.getInstance();
        String startTime = format.format(currentTime.getTime());
        currentTime.add(Calendar.HOUR, 1);
        String endTime = format.format(currentTime.getTime());

        return seatReservationRepository.findByTheaterNumberAndDateIsBetween(theaterNumber, startTime, endTime);
    }

    /**
     * DB에서 특정 상영관, 과거 예약 현황 조회
     *
     * @param theaterNumber Integer
     *                      상영관 번호
     * @param startTime     String
     *                      영화 시작 시간
     * @param endTime       String
     *                      영화 종료 시간
     * @return 예약 목록
     */
    private List<SeatReservationEntity> fetchReservationInfo(int theaterNumber, String startTime, String endTime) {
        return seatReservationRepository.findByTheaterNumberAndDateIsBetween(theaterNumber, startTime, endTime);
    }

    /**
     * reservationRequest 에서 value 추출하여 seatReservation Entity 형태로 만든 후 저장
     *
     * @param reservationRequest ReservationRequest
     *                           예약 요청 메시지
     */
    public void saveEventToDB(ReservationRequest reservationRequest) {
        SeatReservationEntity seatReservationEntity = new SeatReservationEntity();
        seatReservationEntity.extractValuesFromMessageAndSetValues(reservationRequest);
        seatReservationRepository.save(seatReservationEntity);
    }
}
