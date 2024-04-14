package animores.serverapi.diary.entity;

import animores.serverapi.account.domain.Account;
import animores.serverapi.common.BaseEntity;
import animores.serverapi.diary.dto.EditDiaryRequest;
import animores.serverapi.profile.domain.Profile;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

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
    private List<DiaryMedia> media = new ArrayList<>();

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
