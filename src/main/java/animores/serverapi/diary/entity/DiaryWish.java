package animores.serverapi.diary.entity;

import animores.serverapi.account.domain.Account;
import animores.serverapi.profile.domain.Profile;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Getter
@Builder
@EntityListeners(AuditingEntityListener.class)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "diary_wish", uniqueConstraints = {
    @UniqueConstraint(name = "UK_diary_profile", columnNames = {"diary_id", "profile_id"})
})
public class DiaryWish {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Profile profile;

    @ManyToOne(fetch = FetchType.LAZY)
    private Diary diary;


    @CreatedDate
    private LocalDateTime createdAt;

    public static DiaryWish create(Profile profile, Diary diary) {
        return DiaryWish.builder()
            .profile(profile)
            .diary(diary)
            .build();
    }
}
