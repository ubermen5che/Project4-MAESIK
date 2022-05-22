package MAESIK.demo.domain.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class MemberRequestDTO {

    private Long memberId;
    private String oauthId;
    private String name;
    private String email;
    private String imageUrl;
    private String repoUrl;
    private Integer memberExp;
    private String tier;
    private String githubId;
}
