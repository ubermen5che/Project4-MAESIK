package MAESIK.demo.domain.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Data
@Getter
@Setter
public class InviteDTO {
    private Long groupId;
    private List<MemberRequestDTO> memberIds;
}
