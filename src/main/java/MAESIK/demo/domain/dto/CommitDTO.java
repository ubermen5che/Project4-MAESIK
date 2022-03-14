package MAESIK.demo.domain.dto;


import MAESIK.demo.domain.Commit;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Id;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CommitDTO {

    @Id
    private String sha;
    private Commit commit;
    private String url;
}