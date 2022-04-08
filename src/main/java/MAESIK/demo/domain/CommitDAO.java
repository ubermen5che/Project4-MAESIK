package MAESIK.demo.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.sql.Date;


@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommitDAO {

    @GeneratedValue @Id
    private Long commitId;
    private String sha;
    private String url;
    private String name;
    private String email;
    private Date date;
    private String message;
    private String commitUrl;
}
