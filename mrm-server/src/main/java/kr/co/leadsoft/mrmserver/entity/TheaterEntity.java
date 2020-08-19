package kr.co.leadsoft.mrmserver.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;


@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
@Table(name = "theater")
public class TheaterEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "theater_number")
    private int theaterNumber;

    private int totalSeat;

    private String currentShowingMovie;
}
