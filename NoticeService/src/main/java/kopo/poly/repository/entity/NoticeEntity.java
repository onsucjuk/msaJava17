package kopo.poly.repository.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "S_NOTICE")
@DynamicInsert
@DynamicUpdate
@Builder
@Cacheable
@Entity
public class NoticeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notice_seq")
    private Long noticeSeq;

    @NonNull
    @Column(name = "title", length = 500, nullable = false)
    private String title;

    @NonNull
    @Column(name = "notice_yn", length = 1, nullable = false)
    private String noticeYn;

    @NonNull
    @Column(name = "contents", length = 1000, nullable = false)
    private String contents;

    @NonNull
    @Column(name = "user_id", length = 100, nullable = false)
    private String userId;

    @NonNull
    @Column(name = "read_cnt", nullable = false)
    private Long readCnt;

    @NonNull
    @Column(name = "reg_id", length = 100, updatable = false)
    private String regId;

    @NonNull
    @Column(name = "reg_dt", updatable = false)
    private String regDt;

    @NonNull
    @Column(name = "chg_id")
    private String chgId;

    @NonNull
    @Column(name = "chg_dt")
    private String chgDt;

}
