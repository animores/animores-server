package animores.serverapi.account.entity;

import animores.serverapi.account.dto.request.SignUpRequest;
import animores.serverapi.account.type.Role;
import animores.serverapi.common.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.crypto.password.PasswordEncoder;

@Entity
@NoArgsConstructor
@Getter
public class Account extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING)
    private Role role;

    private String email;

    private String password;

    private String nickname;

    private Boolean isAdPermission;

    // 등록, 수정, 탈퇴일 생략

    protected Account(Long id, Role role, String email, String password, String nickname,
        boolean isAdPermission) {
        this.id = id;
        this.role = role;
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.isAdPermission = isAdPermission;
    }

    public static Account toEntity(SignUpRequest request, PasswordEncoder encoder) {
        return new Account(
            null,
            Role.USER,
            request.email(),
            encoder.encode(request.password()),
            request.nickname(),
            false
        );
    }

    public void update(Role role, String email, String password, String nickname,
        Boolean isAdPermission) {
        if (role != null) {
            this.role = role;
        }
        if (StringUtils.isNotBlank(email)) {
            this.email = email;
        }
        if (StringUtils.isNotBlank(password)) {
            this.password = password;
        }
        if (StringUtils.isNotBlank(nickname)) {
            this.nickname = nickname;
        }
        if (isAdPermission != null) {
            this.isAdPermission = isAdPermission;
        }
    }

}
