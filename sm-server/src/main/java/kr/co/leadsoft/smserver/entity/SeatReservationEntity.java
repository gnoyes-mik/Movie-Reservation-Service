package kr.co.leadsoft.smserver.entity;

import kr.co.leadsoft.smserver.model.ReservationRequest;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Getter
@ToString
@Table(name = "seat_reservation")
public class SeatReservationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long no;

    @Column(name = "theater_number")
    private int theaterNumber;

    private int seat;

    private String date;

    public void extractValuesFromMessageAndSetValues(ReservationRequest reservationRequest) {
        this.theaterNumber = reservationRequest.getTheaterNumber();
        this.seat = reservationRequest.getSeat();
        this.date = reservationRequest.getDate();
    }

}
