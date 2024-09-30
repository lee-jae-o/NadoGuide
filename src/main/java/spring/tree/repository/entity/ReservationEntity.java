package spring.tree.repository.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "RESERVATION")
public class ReservationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "RESERVATION_ID")
    private Long reservationId;

    @Column(name = "TOUR_SEQ", nullable = false)
    private Long tourSeq;

    @Column(name = "USER_ID", nullable = false)
    private String userId;

    @Column(name = "RESERVATION_DATE", nullable = false)
    private LocalDateTime reservationDate;




}
