package MAESIK.demo.config;

import MAESIK.demo.jwt.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {
    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String BEARER_PREFIX = "Bearer ";

    @Autowired
    private final TokenService tokenService;

    // Request Header 에서 토큰 정보를 꺼내오기
    private String resolveToken(HttpServletRequest request) {
        System.out.println(request.getHeader(AUTHORIZATION_HEADER));
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
            return bearerToken.substring(7);
        }
        return null;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {

        // 1. Request Header 에서 토큰을 꺼냄
        String jwt = resolveToken(request);

        System.out.println("jwt : " + jwt);
        // 2. validateToken 으로 토큰 유효성 검사
        // 정상 토큰이면 해당 토큰으로 Authentication 을 가져와서 SecurityContext 에 저장
        if (StringUtils.hasText(jwt) && tokenService.verifyToken(jwt)) {
            Authentication authentication = tokenService.getAuthentication(jwt);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);
    }
}