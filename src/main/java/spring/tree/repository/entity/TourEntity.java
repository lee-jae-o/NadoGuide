package spring.tree.repository.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "TOUR_INFO")
@DynamicInsert
@DynamicUpdate
@Builder
@Cacheable
@Entity
public class TourEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TOUR_SEQ")
    private Long tourSeq;

    @NonNull
    @Column(name = "USER_ID", nullable = false)
    private String userId;

    @Column(name = "REG_ID", updatable = false)
    private String regId;

    @Column(name = "reg_dt", updatable = false)
    private String regDt;

    @Column(name = "READ_CNT", nullable = false)
    private Long readCnt;

    @NonNull
    @Column(name = "TOUR_TITLE", nullable = false)
    private String tourTitle;

    @NonNull
    @Column(name = "TOUR_LOCATION", nullable = false)
    private String tourLocation;

    @NonNull
    @Column(name = "TOUR_DATE", nullable = false)
    private LocalDateTime tourDate;

    @NonNull
    @Column(name = "MAX_PARTICIPANTS", nullable = false)
    private Integer maxParticipants = 0;

    @NonNull
    @Column(name = "TOUR_TIME", nullable = false)
    private String tourTime;

    @NonNull
    @Column(name = "SUPPLIES", nullable = false)
    private String supplies;

    @NonNull
    @Column(name = "CATEGORIES", nullable = false)
    private String categories;

    @NonNull
    @Column(name = "TOUR_CONTENTS", nullable = false)
    private String tourContents;

    @Column(name = "IMAGE_URL")
    private String imageUrl;


}
