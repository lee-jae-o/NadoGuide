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
@Table(name = "FAVORITE_TOUR")
public class FavoriteTourEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "USER_ID", nullable = false)
    private String userId;

    @Column(name = "TOUR_SEQ", nullable = false)
    private Long tourSeq;

    @Column(name = "CREATED_AT", nullable = false)
    private LocalDateTime createdAt;
}
