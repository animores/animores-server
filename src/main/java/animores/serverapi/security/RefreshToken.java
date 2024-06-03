package animores.serverapi.security;

import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@Getter
@RedisHash(value = "refreshToken", timeToLive = 5184000)// 2개월
public class RefreshToken {

    @Id
    private String refreshToken;// key

    private Long userId;// value


    public RefreshToken(String refreshToken, Long userId) {
        this.refreshToken = refreshToken;
        this.userId = userId;
    }

}