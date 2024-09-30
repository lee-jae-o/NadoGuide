package spring.tree.repository.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.io.Serializable;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "CALENDAR_INFO")
@DynamicInsert
@DynamicUpdate
@Builder
@Entity
@Cacheable

public class CalendarEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CALENDAR_SEQ")
    private Long calendarSeq;

    @NonNull
    @Column(name = "TITLE", length = 100, nullable = false)
    private String title;

    @NonNull
    @Column(name = "USER_ID", nullable = false)
    private String userId;

    @NonNull
    @Column(name = "START", nullable = false)
    private String start;

    @NonNull
    @Column(name = "END", nullable = false)
    private String end;

    @NonNull
    @Column(name = "DESCRIPTION", length = 2000, nullable = false)
    private String description;
}