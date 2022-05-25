package MAESIK.demo.domain;

import MAESIK.demo.domain.dto.CommitDTO;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class MemberGroupRepoUrl {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MEMBER_GROUP_REPO_ID")
    private Long memberGroupRepoId;
    private String repo;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "memberGroupRepoUrls")
    private List<CommitDAO> commitEachRepo = new ArrayList<>();
}
