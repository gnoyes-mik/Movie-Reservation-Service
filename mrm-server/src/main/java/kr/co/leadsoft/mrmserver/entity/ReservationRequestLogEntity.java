package kr.co.leadsoft.mrmserver.entity;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;


@Entity(name = "reservation_request_log")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class ReservationRequestLogEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long no;

    private String date;

    private int theaterNumber;

    private String movieName;

    private int seat;
}
