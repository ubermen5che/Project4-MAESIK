package MAESIK.demo.config;

import MAESIK.demo.domain.dto.UserDTO;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

@Component
public class UserRequestMapper {
    public UserDTO toDto(OAuth2User oAuth2User) {
        var attributes = oAuth2User.getAttributes();
        return UserDTO.builder()
                .oauthId((String)attributes.get("oauthId"))
                .name((String)attributes.get("name"))
                .imageUrl((String)attributes.get("imageUrl"))
                .build();
    }
}
