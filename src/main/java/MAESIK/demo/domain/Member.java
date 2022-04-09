package MAESIK.demo.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
@ToString
public class Member {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberId;
    private String githubId;
    private String oauthId;
    private String name;
    private String email;
    private String imageUrl;
    private String repoUrl;
    private Integer memberExp;
    private String tier;
    private Boolean emailAuth;

    @OneToMany(cascade = CascadeType.ALL)
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
}
