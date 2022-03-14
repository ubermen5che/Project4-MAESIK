package MAESIK.demo.domain;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
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

}
