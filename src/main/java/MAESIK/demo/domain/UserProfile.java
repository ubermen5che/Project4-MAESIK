package MAESIK.demo.domain;

import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public class UserProfile {
    private final String oauthId;
    private final String name;
    private final String email;
    private final String imageUrl;
    private final String githubId;

    public UserProfile(String oauthId, String name, String email, String imageUrl, String githubId) {
        this.oauthId = oauthId;
        this.name = name;
        this.email = email;
        this.imageUrl = imageUrl;
        this.githubId = githubId;
    }

    public Member toMember() {
        return new Member(oauthId, name, email, imageUrl, Role.USER, githubId);
    }
}
