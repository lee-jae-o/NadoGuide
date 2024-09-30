package spring.tree.repository.entity;


import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "BOARD")
@DynamicInsert
@DynamicUpdate
@Builder
@Cacheable
@Entity
public class BoardEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "BOARD_SEQ")
    private Long boardSeq;

    @NonNull
    @Column(name = "USER_ID", nullable = false)
    private String userId; // 작성자

    @NonNull
    @Column(name = "TITLE", nullable = false)
    private String title;

    @NonNull
    @Column(name = "CONTENTS", nullable = false)
    private String contents;


    @Column(name = "READ_CNT", nullable = false)
    private Long readCnt;


    @Column(name = "REG_ID", updatable = false)
    private String regId; // 등록자 아이디

    @Column(name = "reg_dt", updatable = false)
    private String regDt; // 등록일

    @Column(name = "chg_id")
    private String chgId; // 수정자 아이디

    @Column(name = "chg_dt")
    private String chgDt; // 수정일
}
