package animores.serverapi.account.domain.auth_mail;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.redis.core.RedisHash;

@AllArgsConstructor
@Getter
@RedisHash(value = "validMail", timeToLive = 200)// 10 minute
public class ValidMail {

    private String email;
}
