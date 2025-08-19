package animores.serverapi.filter;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;


/**
 * FirebaseTokenFilter
 *
 * 요청마다 실행되는 Spring Security 커스텀 필터.
 * - Authorization 헤더의 Firebase ID 토큰을 검증
 * - 검증 성공 시 SecurityContextHolder에 인증 정보(uid) 저장
 * - 검증 실패 시 401 Unauthorized 반환
 */
@Component
public class FirebaseTokenFilter extends OncePerRequestFilter {

    /**
     * 매 요청마다 실행됨
     * Firebase ID 토큰을 꺼내 검증 후 SecurityContext에 사용자 인증 정보(uid) 저장
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {


        // ✅ 테스트용 우회 (헤더 userId=13 이면 관리자처럼 통과)
        String testHeader = request.getHeader("userId");
        if ("13".equals(testHeader)) {
            UsernamePasswordAuthenticationToken auth =
                    new UsernamePasswordAuthenticationToken("admin-test", null, Collections.emptyList());
            SecurityContextHolder.getContext().setAuthentication(auth);
            filterChain.doFilter(request, response);
            return;
        }

        // Authorization 헤더에서 토큰 추출
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String idToken = authHeader.substring(7).trim();

            try {
                // Firebase ID 토큰 검증
                FirebaseToken decodedToken = FirebaseAuth.getInstance().verifyIdToken(idToken);
                String uid = decodedToken.getUid();

                // uid를 principal로 설정 (권한 없음)
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(uid, null, Collections.emptyList());

                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // SecurityContextHolder에 인증 정보 저장 (중복 방지)
                if (SecurityContextHolder.getContext().getAuthentication() == null) {
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }

            } catch (FirebaseAuthException e) {
                // 토큰이 잘못되면 401 반환
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid Firebase ID token");
                return;
            }
        }

        // 다음 필터 실행
        filterChain.doFilter(request, response);
    }
}