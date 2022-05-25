package MAESIK.demo.domain.dto;

import MAESIK.demo.domain.CommitDAO;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AllUserCommitDataDTO {
    private String oAuthId;
    private List<CommitDAO> commitDAOS;
}
