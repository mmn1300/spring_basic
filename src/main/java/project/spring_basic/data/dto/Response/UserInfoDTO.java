package project.spring_basic.data.dto.Response;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UserInfoDTO extends ResponseDTO {
    private String id;
    private String nickname;

    public UserInfoDTO(Boolean message, String id, String nickname) {
        super(message);
        this.id = id;
        this.nickname = nickname;
    }
}
