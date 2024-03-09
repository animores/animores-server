package animores.serverapi.config.security;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@RedisHash(value = "blacklistToken", timeToLive = 86400)// 24 hour
public class BlacklistToken {

    @Id
    private String blacklistToken;// key

    private Long userId;// value


    public BlacklistToken(String blacklistToken, Long userId) {
        this.blacklistToken = blacklistToken;
        this.userId = userId;
    }

}