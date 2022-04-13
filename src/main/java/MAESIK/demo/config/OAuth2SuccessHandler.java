package MAESIK.demo.config;

import MAESIK.demo.domain.RefreshToken;
import MAESIK.demo.domain.dto.TokenDto;
import MAESIK.demo.domain.dto.UserDto;
import MAESIK.demo.jwt.TokenService;
import MAESIK.demo.repository.RefreshTokenRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Component
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {
    private final TokenService tokenService;
    private final UserRequestMapper userRequestMapper;
    private final ObjectMapper objectMapper;
    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        OAuth2User oAuth2User = (OAuth2User)authentication.getPrincipal();
        UserDto userDto = userRequestMapper.toDto(oAuth2User);

        //System.out.println("oAuthID : " + oAuth2User.getAttributes().get("id"));
        TokenDto tokenDto = tokenService.generateToken(authentication);

        log.info("{}", tokenDto);

        Optional<RefreshToken> refreshToken = refreshTokenRepository.findByKey(authentication.getName());

        if (refreshToken.isEmpty()) {
            refreshTokenRepository.save(RefreshToken.builder()
                    .key(authentication.getName())
                    .value(tokenDto.getRefreshToken())
                    .build());
        } else {
            refreshToken.get().updateValue(tokenDto.getRefreshToken());
            refreshTokenRepository.save(refreshToken.get());
        }

        writeTokenResponse(response, tokenDto);
    }

    private void writeTokenResponse(HttpServletResponse response, TokenDto token) throws IOException {
        response.setContentType("text/html;charset=UTF-8");

        response.addHeader("Auth", token.getAccessToken());
        response.addHeader("Refresh", token.getRefreshToken());
        response.setContentType("application/json;charset=UTF-8");
        response.sendRedirect("/index.html");
        var writer = response.getWriter();
        writer.println(objectMapper.writeValueAsString(token));
        writer.flush();
    }
}