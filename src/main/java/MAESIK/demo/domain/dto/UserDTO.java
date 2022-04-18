package MAESIK.demo.domain.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class UserDto {
    private String oauthId;
    private String name;
    private String picture;

    @Builder
    public UserDto(String oauthId, String name, String picture) {
        this.oauthId = oauthId;
        this.name = name;
        this.picture = picture;
    }
}
