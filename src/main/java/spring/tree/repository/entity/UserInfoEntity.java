package spring.tree.repository.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.io.Serializable;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "USER_INFO")
@DynamicInsert
@DynamicUpdate
@Builder
@Cacheable
@Entity
public class UserInfoEntity implements Serializable {

    @Id
    @Column(name = "USER_ID")
    private String userId;

    @NonNull
    @Column(name = "USER_NAME", length = 100, nullable = false)
    private String userName;

    @NonNull
    @Column(name = "PASSWORD", length = 255, nullable = false)
    private String password;

    @NonNull
    @Column(name = "EMAIL", nullable = false)
    private String email;

    @NonNull
    @Column(name = "NICK_NAME", nullable = false)
    private String nickName;

    @NonNull
    @Column(name = "ADDR1", nullable = false)
    private String addr1;

    @Column(name = "ADDR2", nullable = false)
    private String addr2;

    @Column(name = "reg_id", updatable = false)
    private String regId;

    @Column(name = "reg_dt", updatable = false)
    private String regDt;

    @Column(name = "chg_id")
    private String chgId;

    @Column(name = "chg_dt")
    private String chgDt;

    @Column(name = "roles") //권한 데이터는 ,를 구분자로 여러 개(예 : 관리자, 일반사용자) 정의 가능함
    private String roles;



}