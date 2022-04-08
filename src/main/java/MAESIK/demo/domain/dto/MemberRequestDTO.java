package MAESIK.demo.domain.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MemberRequestDTO {

    @Builder
    public MemberRequestDTO(Long memberId, String oauthId, String name, String email, String imageUrl, String repoUrl, Integer memberExp, String tier) {
        this.memberId = memberId;
        this.oauthId = oauthId;
        this.name = name;
        this.email = email;
    }

    private Long memberId;
    private String oauthId;
    private String name;
    private String email;
    private String imageUrl;
    private String repoUrl;
    private Integer memberExp;
    private String tier;
}
