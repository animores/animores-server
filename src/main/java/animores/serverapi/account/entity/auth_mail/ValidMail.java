package animores.serverapi.account.entity.auth_mail;

import org.springframework.data.annotation.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.redis.core.RedisHash;

@AllArgsConstructor
@Getter
@RedisHash(value = "validMail", timeToLive = 200)// 10 minute
public class ValidMail {
    @Id
    private String email;
}
