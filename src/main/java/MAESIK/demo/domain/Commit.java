package MAESIK.demo.domain;

import MAESIK.demo.domain.dto.Author;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Commit {
    private Author author;
    private String message;
    private String url;
}
