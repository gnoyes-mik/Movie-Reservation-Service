package kr.co.leadsoft.smserver.controller;

import kr.co.leadsoft.smserver.service.SeatManagerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;


@RequestMapping
@RestController
@RequiredArgsConstructor
public class SeatManagerController {

    private final SeatManagerService seatManagerService;

    @GetMapping("/seats/reserved/{theaterNumber}")
    public HashMap<String, Object> fetchTheaterStatus(@PathVariable int theaterNumber) {
        return seatManagerService.getReservationStatus(theaterNumber);
    }

    @GetMapping("/reservations/history")
    public HashMap<String, Object> fetchPastTheaterStatus(@RequestParam int theaterNumber,
                                                          @RequestParam String startTime,
                                                          @RequestParam String endTime) {
        return seatManagerService.getReservationStatus(theaterNumber, startTime, endTime);
    }
}
