package MAESIK.demo.domain;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@Builder
@Table(name = "members")
@ToString
public class Member {

    public Member(Long id, String githubId, String oauthId, String name, String email, String imageUrl, String repoUrl, Integer memberExp, String tier, Boolean emailAuth, List<MemberGroup> memberGroupList, Role role) {
        this.id = id;
        this.githubId = githubId;
        this.oauthId = oauthId;
        this.name = name;
        this.email = email;
        this.imageUrl = imageUrl;
        this.repoUrl = repoUrl;
        this.memberExp = memberExp;
        this.tier = tier;
        this.emailAuth = emailAuth;
        this.memberGroupList = memberGroupList;
        this.role = role;
    }

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String githubId;
    private String oauthId;
    private String name;
    private String email;
    private String imageUrl;
    private String repoUrl;
    private Integer memberExp;
    private String tier;
    private Boolean emailAuth;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "member_id")
    private List<MemberGroup> memberGroupList = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private Role role;

    public Member(String oauthId, String name, String email, String imageUrl, Role user, String githubId) {
        this.oauthId = oauthId;
        this.name = name;
        this.email = email;
        this.imageUrl = imageUrl;
        this.role = user;
        this.githubId = githubId;
    }

    public Member(String oauthId, Role role) {
        this.oauthId = oauthId;
        this.role = role;
    }

    public Member update(String name, String email, String imageUrl, String githubId) {
        this.name = name;
        this.email = email;
        this.imageUrl = imageUrl;
        this.githubId = githubId;
        return this;
    }

    public String getRoleKey() {
        return this.role.getKey();
    }

    public String getOauthId() {
        return oauthId;
    }

    public void updateEmail(String email) {
        this.email = email;
    }

    public void updateEmailAuth(boolean emailAuth) {
        this.emailAuth = emailAuth;
    }
}
