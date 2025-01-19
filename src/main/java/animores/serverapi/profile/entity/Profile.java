package animores.serverapi.profile.entity;

import static animores.serverapi.common.S3Path.PROFILE_IMAGE_PATH;

import animores.serverapi.account.entity.Account;
import animores.serverapi.common.BaseEntity;
import animores.serverapi.profile.dto.request.ProfileCreateRequest;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Profile extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    // 일대다 계정
    @ManyToOne(fetch = FetchType.LAZY)
    private Account account;

    private String name;

    private String imageUrl;

    private String pushToken;
    private LocalDateTime deletedAt;

    private static final String DEFAULT_PROFILE_IMAGE_URL =
        PROFILE_IMAGE_PATH + "default_profile.png";

    public String getImageUrl() {
        return this.imageUrl == null ? DEFAULT_PROFILE_IMAGE_URL : this.imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void delete() {
        this.deletedAt = LocalDateTime.now();
    }

    public static Profile create(Account account, ProfileCreateRequest request, String imageUrl) {
        return new Profile(
            null,
            account,
            request.name(),
            imageUrl,
            null,
            null
        );
    }
}
