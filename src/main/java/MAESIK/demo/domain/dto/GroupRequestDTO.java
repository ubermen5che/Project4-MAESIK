package MAESIK.demo.domain.dto;

import MAESIK.demo.domain.Group;
import MAESIK.demo.domain.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
public class GroupRequestDTO {

    private String groupName;
    private String repoUrl;
    private String updateUrl;
    private Long groupId;
    private List<String> invitedMemberList;

    @Builder
    public GroupRequestDTO(String groupName) {
        this.groupName = groupName;
    }
}
