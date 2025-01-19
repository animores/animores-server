package animores.serverapi.security;

import animores.serverapi.account.type.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;
import javax.crypto.spec.SecretKeySpec;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

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
     *
     * @param email
     * @return
     */
    public String createToken(String email) {

        return Jwts.builder()
            .signWith(new SecretKeySpec(secretKey.getBytes(),
                SignatureAlgorithm.HS512.getJcaName())) // HS512 알고리즘을 사용하여 secretKey를 이용해 서명
            .setSubject(email) // JWT 토큰 제목
            .setIssuer(issuer) // JWT 토큰 발급자
            .setIssuedAt(Timestamp.valueOf(LocalDateTime.now())) // JWT 토큰 발급 시간
            .setExpiration(new Date(
                System.currentTimeMillis() + (long) expirationHours * 3600 * 1000)) // JWT 토큰 만료 시간
            .compact(); // JWT 토큰 생성
    }

    public String getEmailFromToken(String token) {

        if (token.split(" ").length == 2) {
            token = token.split(" ")[1];
        }

        Claims claims = Jwts.parserBuilder()
                .setSigningKey(
                        new SecretKeySpec(secretKey.getBytes(),
                        SignatureAlgorithm.HS512.getJcaName()))
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }
}