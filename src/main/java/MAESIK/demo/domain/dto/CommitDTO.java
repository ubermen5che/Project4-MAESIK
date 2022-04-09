package MAESIK.demo.domain.dto;


import MAESIK.demo.domain.Commit;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CommitDTO {

    private String sha;
    private Commit commit;
    private String url;
}