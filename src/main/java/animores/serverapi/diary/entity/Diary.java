package animores.serverapi.diary.entity;

import animores.serverapi.account.domain.Account;
import animores.serverapi.common.BaseEntity;
import animores.serverapi.diary.dto.EditDiaryRequest;
import animores.serverapi.profile.domain.Profile;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Builder
@EntityListeners(AuditingEntityListener.class)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "diary")
public class Diary extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Profile profile;

    @ManyToOne(fetch = FetchType.LAZY)
    private Account account;

    @OneToMany(mappedBy = "diary", cascade = CascadeType.ALL)
    private List<DiaryMedia> media;

    // List<DiaryComment> 연결 예정

    @Lob
    @Column(nullable = false)
    private String content;


    private LocalDateTime deletedDt;

    public static Diary create(Account account, Profile profile, String content) {
        return Diary.builder()
            .account(account)
            .profile(profile)
            .content(content)
            .build();
    }

    public void update(EditDiaryRequest request) {
        this.content = request.content();
    }

    public void delete() {
        this.deletedDt = LocalDateTime.now();
    }

}
