package kopo.poly.repository.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "S_USER_INFO")
@DynamicInsert
@DynamicUpdate
@Builder
@Cacheable
@Entity
public class UserInfoEntity {

    @Id
    @Column(name = "USER_ID")
    private String userId;

    @NonNull
    @Column(name = "USER_NAME", length = 100, nullable = false)
    private String userName;

    @NonNull
    @Column(name = "PASSWORD", length = 100, nullable = false)
    private String password;

    @NonNull
    @Column(name = "EMAIL", nullable = false)
    private String email;

    @NonNull
    @Column(name = "ADDR1", nullable = false)
    private String addr1;

    @Column(name = "ADDR2", nullable = false)
    private String addr2;

    @Column(name = "reg_id", updatable = false)
    private String regId;

    @Column(name = "reg_dt", updatable = false)
    private String regDt;

    @Column(name = "chg_id", updatable = false)
    private String chgId;

    @Column(name = "chg_dt", updatable = false)
    private String chgDt;

    @Column(name = "roles") // 권한 데이터는 ,를 구분자로 여러 개 정의 가능
    private String roles;

}
