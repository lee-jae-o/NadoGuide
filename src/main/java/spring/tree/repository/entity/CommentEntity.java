package spring.tree.repository.entity;


import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "COMMENT")
@DynamicInsert
@DynamicUpdate
@Builder
@Cacheable
@Entity
@IdClass(CommentPK.class)
public class CommentEntity {

    @Id
    @Column(name = "BOARD_SEQ")
    private Long boardSeq;

    @Id
    @Column(name = "COMMENT_SEQ")
    private Long commentSeq;

    @NonNull
    @Column(name = "COMMENT", nullable = false)
    private String comment;

    @NonNull
    @Column(name = "USER_ID", nullable = false)
    private String userId;

    @Column(name = "reg_dt", updatable = false)
    private String regDt;

    @Column(name = "chg_dt")
    private String chgDt;


}
