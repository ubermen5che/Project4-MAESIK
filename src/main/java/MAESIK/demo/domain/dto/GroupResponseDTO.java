package MAESIK.demo.domain.dto;

import MAESIK.demo.domain.Group;
import lombok.*;

@Getter
@Setter
@Builder
@Data
public class GroupResponseDTO {

    public GroupResponseDTO(Long groupId, String groupName, Integer groupExp, String groupTier, String groupMasterId) {
        this.groupId = groupId;
        this.groupName = groupName;
        this.groupExp = groupExp;
        this.groupTier = groupTier;
        this.groupMasterId = groupMasterId;
    }

    private Long groupId;
    private String groupName;
    private Integer groupExp;
    private String groupTier;
    private String groupMasterId;
}
