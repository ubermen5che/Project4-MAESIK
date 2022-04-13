package MAESIK.demo.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Table(name = "refresh_token")
@Entity
public class RefreshToken {

    @Id @GeneratedValue
    @Column(name = "rt_id")
    private Long id;

    @Column(name = "rt_key")   //oauthId
    private String key;

    @Column(name = "rt_value") //token
    private String value;

    @Builder
    public RefreshToken(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public RefreshToken updateValue(String token) {
        this.value = value;
        return this;
    }
}
