package kr.co.leadsoft.mrmserver.service;

import kr.co.leadsoft.mrmserver.entity.ReservationRequestLogEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;


@Service
public class KafkaProducer {

    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaProducer.class);

    @Value(value = "${message.topic.name}")
    private String topicName;

    private final KafkaTemplate<String, ReservationRequestLogEntity> kafkaTemplate;

    @Autowired
    public KafkaProducer(KafkaTemplate<String, ReservationRequestLogEntity> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendMessage(ReservationRequestLogEntity message) {

        ListenableFuture<SendResult<String, ReservationRequestLogEntity>> future = kafkaTemplate.send(topicName, message);

        future.addCallback(new ListenableFutureCallback<SendResult<String, ReservationRequestLogEntity>>() {

            @Override
            public void onSuccess(SendResult<String, ReservationRequestLogEntity> result) {
                LOGGER.info("Sent message=[" + message + "] with offset=[" + result.getRecordMetadata().offset() + "]");
            }

            @Override
            public void onFailure(@NonNull Throwable ex) {
                LOGGER.info("Unable to send message=[" + message + "] due to : " + ex.getMessage());
            }
        });
    }
}