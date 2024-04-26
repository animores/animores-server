package animores.serverapi.account.domain.auth_mail;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.redis.core.RedisHash;

@AllArgsConstructor
@Getter
@RedisHash(value = "authMail", timeToLive = 200)// 10 minute
public class AuthMail {

        private String email;
        private String code;
}
