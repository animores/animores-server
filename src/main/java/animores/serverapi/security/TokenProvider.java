package animores.serverapi.security;

import animores.serverapi.account.type.Role;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.spec.SecretKeySpec;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;

@Service
@Getter
public class TokenProvider {
    private final String secretKey;
    private final int expirationHours;
    private final String issuer;

    public TokenProvider(@Value("${spring.jwt.secret-key}") String secretKey,
                         @Value("${spring.jwt.expiration-hours}") int expirationHours,
                         @Value("${spring.jwt.issuer}") String issuer) {
        this.secretKey = secretKey;
        this.expirationHours = expirationHours;
        this.issuer = issuer;
    }

    /**
     * 토큰 생성
     * @param accountId
     * @param role
     * @return
     */
    public String createToken(Long accountId, Role role) {


        return Jwts.builder()
                .signWith(new SecretKeySpec(secretKey.getBytes(), SignatureAlgorithm.HS512.getJcaName())) // HS512 알고리즘을 사용하여 secretKey를 이용해 서명
                .setSubject(String.format("%s:%s", accountId, role)) // JWT 토큰 제목
                .setIssuer(issuer) // JWT 토큰 발급자
                .setIssuedAt(Timestamp.valueOf(LocalDateTime.now())) // JWT 토큰 발급 시간
                .setExpiration(new Date(System.currentTimeMillis() + (long) expirationHours * 3600 * 1000)) // JWT 토큰 만료 시간
                .compact(); // JWT 토큰 생성
    }

    /**
     * 토큰 복호화
     * @param token
     * @return
     */
    public String validateTokenAndGetSubject(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey.getBytes())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

}