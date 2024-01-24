package animores.serverapi.profile.domain;

import animores.serverapi.account.domain.Account;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Getter
@Builder
public class Profile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    protected Profile(Long id) {
        this.id = id;
    }

    // 일대다 계정
    @ManyToOne(fetch = FetchType.LAZY)
    private Account account;

    private String name;

    private String imageUrl;

    private String pushToken;

    // 생성, 수정, 삭제 생략


    public Profile(Long id, Account account, String name, String imageUrl, String pushToken) {
        this.id = id;
        this.account = account;
        this.name = name;
        this.imageUrl = imageUrl;
        this.pushToken = pushToken;
    }

}
