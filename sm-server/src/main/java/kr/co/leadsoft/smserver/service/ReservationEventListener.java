package kr.co.leadsoft.smserver.service;

import kr.co.leadsoft.smserver.model.ReservationRequest;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class ReservationEventListener {

    private static final Logger LOG = LoggerFactory.getLogger(ReservationEventListener.class);

    private final SeatManagerService seatManagerService;


    @KafkaListener(topics = "${message.topic.name}", groupId = "${kafka.groupId}")
    public void listen(@Payload ReservationRequest reservationRequest) {
        seatManagerService.saveEventToDB(reservationRequest);
        LOG.info("received message='{}'", reservationRequest);
    }
}
