package MAESIK.demo.domain.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class UserDTO {
    private String oauthId;
    private String name;
    private String imageUrl;

    @Builder
    public UserDTO(String oauthId, String name, String imageUrl) {
        this.oauthId = oauthId;
        this.name = name;
        this.imageUrl = imageUrl;
    }
}
