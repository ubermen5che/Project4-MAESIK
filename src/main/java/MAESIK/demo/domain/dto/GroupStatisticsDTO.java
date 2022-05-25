package MAESIK.demo.domain.dto;

import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class GroupStatisticsDTO {
    private String username;
    private int numOfCommit;
}
