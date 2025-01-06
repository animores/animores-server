package animores.serverapi.account.entity.auth_mail;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@AllArgsConstructor
@Getter
@RedisHash(value = "authMail", timeToLive = 200)// 10 minute
public class AuthMail {

    @Id
    private String code;
    private String email;
}
