package animores.serverapi.account.domain;

import animores.serverapi.account.request.SignUpRequest;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

@Entity
@NoArgsConstructor
@Getter
@Builder
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;

    private String password;

    private String nickname;

    private String phone;

    private boolean isAdPermission;

    // 등록, 수정, 탈퇴일 생략

    protected Account(Long id, String email, String password, String nickname, String phone, boolean isAdPermission) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.phone = phone;
        this.isAdPermission = isAdPermission;
    }

    public static Account toEntity(SignUpRequest request, PasswordEncoder encoder) {
        return Account.builder()
                .email(request.email())
                .password(encoder.encode(request.password()))// 암호화
                .nickname(request.nickname())
                // phone 생략
                .isAdPermission(request.isAdPermission())
                .build();
    }

}
