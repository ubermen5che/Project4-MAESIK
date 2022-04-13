package MAESIK.demo.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@Table(name = "member_group")
@Entity
@Builder
@AllArgsConstructor
public class MemberGroup implements Serializable {

    @Id @GeneratedValue
    private Long memberGroupId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id")
    private Group group;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "member_group_id")
    private List<MemberGroupRepoUrl> memberGroupRepoUrlList = new ArrayList<>();
}
