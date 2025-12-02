package animores.serverapi.config;

import animores.serverapi.filter.FirebaseTokenFilter;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;

/**
 * FirebaseConfig
 *
 * - Firebase Admin SDK 초기화
 * - Spring Security 설정 (FirebaseTokenFilter 등록 포함)
 */
@Configuration
public class FirebaseConfig {
    public void initialize() throws IOException {
        InputStream serviceAccount = getClass()
                .getClassLoader()
                .getResourceAsStream("firebase-service-account.json");

        if (serviceAccount == null) {
            throw new IllegalStateException("firebase-service-account.json 누락");
        }

        FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .build();

        if (FirebaseApp.getApps().isEmpty()) {
            FirebaseApp.initializeApp(options);
        }
    }

    /**
     * Spring Security 필터 체인 정의
     * - 특정 경로는 permitAll()
     * - 나머지는 인증 필요
     * - FirebaseTokenFilter를 UsernamePasswordAuthenticationFilter 앞에 등록
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http,
                                                   FirebaseTokenFilter firebaseTokenFilter) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/public/**",
                                "/h2-console/**",
                                "/swagger-ui/**",
                                "/v3/api-docs/**",
                                "/swagger-resources/**",
                                "/api/v1/account/check-nickname/**"
                        ).permitAll()
                        .anyRequest().authenticated()
                )
                .headers(h -> h.frameOptions(f -> f.sameOrigin())) // H2 콘솔 접근 허용
                .addFilterBefore(firebaseTokenFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
