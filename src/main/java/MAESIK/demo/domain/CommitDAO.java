package MAESIK.demo.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Date;
import java.time.LocalDate;


@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommitDAO {

    @GeneratedValue @Id
    @Column(name = "COMMIT_ID")
    private Long commitId;
    private String sha;
    private String url;
    private String name;
    private String email;
    private Long commitDate;
    private String message;
    private String commitUrl;
    private Long memberGroupRepoId;

    @ManyToOne(fetch = FetchType.LAZY)
    private MemberGroupRepoUrl memberGroupRepoUrls;

}
