package MAESIK.demo.domain;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "members")
public class Member {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberId;
    private String oauthId;
    private String name;
    private String email;
    private String imageUrl;
    private String repoUrl;
    private Integer memberExp;
    private String tier;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "member_id")
    private List<MemberGroup> memberGroupList = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private Role role;

    public Member(String oauthId, String name, String email, String imageUrl, Role user, String s) {
    }

    public Member update(String name, String email, String imageUrl) {
        this.name = name;
        this.email = email;
        this.imageUrl = imageUrl;
        return this;
    }

    public String getRoleKey() {
        return this.role.getKey();
    }

    public String getOauthId() {
        return oauthId;
    }
}
