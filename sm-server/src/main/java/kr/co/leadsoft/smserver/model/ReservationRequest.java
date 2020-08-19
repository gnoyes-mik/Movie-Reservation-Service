package kr.co.leadsoft.smserver.model;

import lombok.*;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class ReservationRequest {

    private Long no;

    private String date;

    private int theaterNumber;

    private String movieName;

    private int seat;

    private boolean completedFlag;
}
