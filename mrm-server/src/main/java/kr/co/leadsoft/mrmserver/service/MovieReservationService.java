package kr.co.leadsoft.mrmserver.service;

import kr.co.leadsoft.mrmserver.component.ReservationGenerator;
import kr.co.leadsoft.mrmserver.entity.ReservationRequestLogRepository;
import kr.co.leadsoft.mrmserver.entity.ReservationRequestLogEntity;
import kr.co.leadsoft.mrmserver.entity.TheaterEntity;
import kr.co.leadsoft.mrmserver.entity.TheaterEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;


@Service
@RequiredArgsConstructor
public class MovieReservationService {

    private final ReservationRequestLogRepository reservationRequestLogRepository;
    private final SMProxy smProxy;
    private final KafkaProducer producer;
    private final ReservationGenerator reservationGenerator;
    private final TheaterEntityRepository theaterEntityRepository;

    /**
     * 상영관 현황 조회
     *
     * @param numberList List<String>
     *                   조회하고자 하는 상영관 번호 목록
     * @return List<HashMap < String, Object>>
     */
    public List<HashMap<String, Object>> fetchTheaterStatus(List<String> numberList) {
        List<HashMap<String, Object>> theaterList = new ArrayList<>();
        if (numberList.contains("0")) {
            numberList = getAllTheaterList();
        }
        System.out.println(numberList.toString());
        for (String theaterNum : numberList) {
            HashMap<String, Object> theaterStatus = smProxy.checkTheaterStatus(Integer.parseInt(theaterNum));
            TheaterEntity theaterEntity = theaterEntityRepository.findByTheaterNumber(Integer.parseInt(theaterNum));
            theaterStatus.put("theater_number", theaterEntity.getTheaterNumber());
            theaterStatus.put("movie_name", theaterEntity.getCurrentShowingMovie());
            theaterStatus.put("total_seat", theaterEntity.getTotalSeat());
            theaterList.add(theaterStatus);
        }
        return theaterList;
    }

    /**
     * 전체 상영관 리스트 조회
     *
     * @return List<String>
     */
    private List<String> getAllTheaterList() {
        List<TheaterEntity> allTheaterList = theaterEntityRepository.findAll();
        List<String> theaterSet = new ArrayList<>();
        for (TheaterEntity theater : allTheaterList) {
            theaterSet.add(Integer.toString(theater.getTheaterNumber()));
        }
        return theaterSet;
    }

    /**
     * 특정 상영관의 남은 좌석수 확인하여 예약 가능한 상태인지 조회
     *
     * @param theaterNum Integer
     *                   상영관 번호
     * @return boolean
     */
    public boolean checkRemainSeat(int theaterNum) {
        HashMap<String, Object> theaterStatus = smProxy.checkTheaterStatus(theaterNum);
        TheaterEntity theaterEntity = theaterEntityRepository.findByTheaterNumber(theaterNum);
        theaterStatus.put("total_seat", theaterEntity.getTotalSeat());
        return (Integer) theaterStatus.get("reserved_seat") < (Integer) theaterStatus.get("total_seat");
    }

    /**
     * 예약 메시지로 부터 상영관 번호, 좌석 정보를 추출하여 현재 상영하는 영화의 중복 예약 여부 확인
     *
     * @param theaterNum Integer
     *                   상영관 번호
     * @param seat       Integer
     *                   좌석 번호
     * @return boolean
     */
    public boolean checkDuplicationReservation(int theaterNum, int seat) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:00:00");
        Calendar currentTime = Calendar.getInstance();
        String startTime = format.format(currentTime.getTime());
        currentTime.add(Calendar.HOUR, 1);
        String endTime = format.format(currentTime.getTime());

        ReservationRequestLogEntity requestLog = reservationRequestLogRepository.findByTheaterNumberAndDateIsBetweenAndSeat(
                theaterNum, startTime, endTime, seat
        );
        return requestLog != null;
    }

    /**
     * 예약 요청 메시지 저장
     *
     * @param request ReservationRequestLogEntity
     *                예약 메시지
     */
    public void saveEventToDB(ReservationRequestLogEntity request) {
        reservationRequestLogRepository.save(request);
    }


    /**
     * 예약 이벤트 Kafka로 produce
     *
     * @param event ReservationRequestLogEntity
     *              예약 메시지
     */
    public void sendEventToKafka(ReservationRequestLogEntity event) {
        producer.sendMessage(event);
    }

    /**
     * times 만큼 예약 메시지 생성 후 요청
     *
     * @param times Integer
     *              횟수
     */
    public void generateReservation(int times) {
        for (int k = 0; k < times; k++) {
            reservationGenerator.generateReservation();
        }
    }
}
