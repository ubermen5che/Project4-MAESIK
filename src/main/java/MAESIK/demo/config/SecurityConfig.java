package MAESIK.demo.config;

import MAESIK.demo.jwt.TokenService;
import MAESIK.demo.service.OAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.logout.LogoutFilter;


@EnableWebSecurity // spring security 설정을 활성화시켜주는 어노테이션
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    public SecurityConfig(OAuthService oAuthService, OAuth2SuccessHandler successHandler, JwtAuthFilter jwtAuthFilter, TokenService tokenService) {
        this.oAuthService = oAuthService;
        this.successHandler = successHandler;
        this.jwtAuthFilter = jwtAuthFilter;
        this.tokenService = tokenService;
    }

    private final OAuthService oAuthService;
    private final OAuth2SuccessHandler successHandler;
    private final JwtAuthFilter jwtAuthFilter;
    private final TokenService tokenService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers("/auth/**", "/index.html", "/login", "/oauth2").permitAll()
                .anyRequest().authenticated()
                .and()
                .apply(new JwtSecurityConfig(tokenService))
                .and()
                .oauth2Login() // OAuth2 로그인 설정 시작점
                .successHandler(successHandler)
                .userInfoEndpoint() // OAuth2 로그인 성공 이후 사용자 정보를 가져올 때 설정 담당
                .userService(oAuthService); // OAuth2 로그인 성공 시, 후작업을 진행할 UserService 인터페이스 구현체 등록

    }
}

