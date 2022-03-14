package MAESIK.demo.domain;

/**
import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GroupRepo {

    @Id
    @NotNull
    private String groupId;
    private String OAuthId;
    private String repoUrl;
    private String repoName;
    @ManyToOne(targetEntity = Group.class, fetch = FetchType.LAZY)
    private Group group;
}
**/