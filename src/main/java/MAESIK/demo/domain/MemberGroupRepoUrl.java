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
@Table(name = "member_group_repo_urls")
public class MemberGroupRepoUrl {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberGroupRepoId;
    private String repo;
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "member_group_repo_id")
    private List<CommitDAO> commitEachRepo = new ArrayList<>();
}
