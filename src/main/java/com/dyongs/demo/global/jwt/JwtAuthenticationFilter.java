package com.dyongs.demo.global.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;

    // 🔒 여기 적힌 경로들은 로그인 필수
    private static final List<String> PROTECTED_PATHS = List.of(
            "/product"    // /product, /product/1, /product/1/update 등
    );

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        String uri = request.getRequestURI();
        String method = request.getMethod();

        // 1) 토큰 검사 안 해도 되는 경로 먼저 통과
        if (isExcludedPath(uri, method)) {
            filterChain.doFilter(request, response);
            return;
        }

        // 2) 보호 안 하는 경로면 그냥 통과
        if (!isProtectedPath(uri, method)) {
            filterChain.doFilter(request, response);
            return;
        }

        // 3) 여기까지 왔다 = 토큰 필수인 경로
        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            writeUnauthorized(response, "Authorization 헤더가 없거나 형식이 올바르지 않습니다.");
            return;
        }

        String token = authHeader.substring(7);

        if (!jwtTokenProvider.validateToken(token)) {
            writeUnauthorized(response, "유효하지 않은 또는 만료된 토큰입니다.");
            return;
        }

        // 토큰 유효 → userId를 request에 심어서 뒤에서 쓸 수 있게
        Long userId = jwtTokenProvider.getUserId(token);
        request.setAttribute("userId", userId);

        // 디버깅용 로그
        log.debug("JWT 인증 완료. userId={}", userId);

        // 다음 필터 / 컨트롤러로 진행
        filterChain.doFilter(request, response);
    }

    private boolean isExcludedPath(String uri, String method) {
        // 토큰 없이 항상 허용
        if (uri.startsWith("/auth") || uri.startsWith("/error") || uri.startsWith("/actuator")) {
            return true;
        }

        // product 조회(GET)는 예외로 허용
        if (uri.startsWith("/product") && "GET".equalsIgnoreCase(method)) {
            return true;
        }

        return false;
    }

    private boolean isProtectedPath(String uri, String method) {
        // product에 대해 "쓰기" 요청만 보호
        if (uri.startsWith("/product")
                && ("POST".equalsIgnoreCase(method)
                || "PUT".equalsIgnoreCase(method)
                || "DELETE".equalsIgnoreCase(method)
                || "PATCH".equalsIgnoreCase(method))) {
            return true;
        }

        return false;
    }

    private void writeUnauthorized(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType("application/json;charset=UTF-8");
        String body = """
                {
                  "success": false,
                  "code": "UNAUTHORIZED",
                  "message": "%s"
                }
                """.formatted(message);
        response.getWriter().write(body);
    }
}
