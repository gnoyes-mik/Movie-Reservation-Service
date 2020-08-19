package kr.co.leadsoft.mrmserver.controller;

import kr.co.leadsoft.mrmserver.entity.ReservationRequestLogEntity;
import kr.co.leadsoft.mrmserver.service.MovieReservationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Slf4j
@RestController
@RequestMapping
@RequiredArgsConstructor
public class MovieReservationController {

    private final MovieReservationService movieReservationService;

    @GetMapping("/theaters/information")
    public List<HashMap<String, Object>> SearchMovieInfo(@RequestParam(value = "theaterNumber") String theaters) {
        String[] strList = theaters.split(",");
        List<String> theaterSet = Arrays.asList(strList);
        return movieReservationService.fetchTheaterStatus(theaterSet);
    }

    @PostMapping("/reservations")
    public ResponseEntity<ReservationResponse> reserveMovie(@RequestBody ReservationRequestLogEntity reservationRequest) {
        ReservationResponse response = new ReservationResponse();
        // 만석 여부 확인
        if (!movieReservationService.checkRemainSeat(reservationRequest.getTheaterNumber())) {
            response.setMessage("자리가 모두 예약 되어있습니다.");
            return ResponseEntity.badRequest().body(response);
        }
        // 중복 여부
        if (movieReservationService.checkDuplicationReservation(reservationRequest.getTheaterNumber(), reservationRequest.getSeat())) {
            response.setMessage("이미 예약된 자리 입니다.");
            return ResponseEntity.badRequest().body(response);
        }
        movieReservationService.saveEventToDB(reservationRequest);
        movieReservationService.sendEventToKafka(reservationRequest);
        response.setMessage("예약 완료");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/auto-reservation/{times}")
    public void GenerateReservation(@PathVariable int times) {
        movieReservationService.generateReservation(times);
    }
}
