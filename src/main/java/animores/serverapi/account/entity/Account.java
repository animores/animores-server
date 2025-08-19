package animores.serverapi.account.entity;

import animores.serverapi.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

@Entity
@NoArgsConstructor
@Getter
public class Account extends BaseEntity {

    @Id
    private String id;

    @Column(unique = true, nullable = true)
    private String nickname;

    @Column(nullable = false)
    private Boolean isAdPermission = false;

    protected Account(String id, String nickname, boolean isAdPermission) {
        this.id = id;
        this.nickname = nickname;
        this.isAdPermission = isAdPermission;
    }

    /** uid-only 자동 회원가입용 팩토리 */
    public static Account createWithUid(String uid) {
        return new Account(uid, null, false);
    }

    public void update(String nickname, Boolean isAdPermission) {
        if (StringUtils.isNotBlank(nickname)) {
            this.nickname = nickname;
        }
        if (isAdPermission != null) {
            this.isAdPermission = isAdPermission;
        }
    }
}
