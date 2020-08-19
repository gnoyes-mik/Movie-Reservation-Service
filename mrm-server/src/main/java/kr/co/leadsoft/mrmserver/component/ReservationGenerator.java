package kr.co.leadsoft.mrmserver.component;

import kr.co.leadsoft.mrmserver.entity.TheaterEntity;
import kr.co.leadsoft.mrmserver.entity.TheaterEntityRepository;
import kr.co.leadsoft.mrmserver.service.MRMProxy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

@Component
@RequiredArgsConstructor
public class ReservationGenerator {

    private final MRMProxy mrmProxy;
    private final TheaterEntityRepository theaterEntityRepository;
    private HashMap<String, String> movieList;

    @PostConstruct
    public void init() {
        movieList = new HashMap<>();
        List<TheaterEntity> theaterEntities = theaterEntityRepository.findAll();
        for (TheaterEntity entity : theaterEntities) {
            movieList.put(Integer.toString(entity.getTheaterNumber()), entity.getCurrentShowingMovie());
        }
    }

    public String getRandomNumber(int range) {
        Random random = new Random();
        return Integer.toString(random.nextInt(range) + 1);
    }

    public String getCurrentTime() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar currentTime = Calendar.getInstance();
        return format.format(currentTime.getTime());
    }

    public void generateReservation() {
        HashMap<String, String> reservationRequest = new HashMap<>();
        String theaterNumber = getRandomNumber(20);
        reservationRequest.put("theaterNumber", theaterNumber);
        reservationRequest.put("movieName", movieList.get(theaterNumber));
        reservationRequest.put("seat", getRandomNumber(500));
        reservationRequest.put("date", getCurrentTime());

        mrmProxy.reserveMovie(reservationRequest);
    }
}
