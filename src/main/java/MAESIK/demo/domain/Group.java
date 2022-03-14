package MAESIK.demo.domain;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "`groups`")
public class Group {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @Builder
    public Group(Long id, String groupName, Integer groupExp, String groupTier, String groupMasterId) {
        this.id = id;
        this.groupName = groupName;
        this.groupExp = groupExp;
        this.groupTier = groupTier;
        this.groupMasterId = groupMasterId;
    }

    private String groupName;
    private Integer groupExp;
    private String groupTier;
    private String groupMasterId;

}
