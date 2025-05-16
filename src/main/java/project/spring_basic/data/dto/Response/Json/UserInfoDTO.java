package project.spring_basic.data.dto.Response.Json;

import java.util.Optional;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@ToString
public class UserInfoDTO extends ResponseDTO {

    private String id;

    @Setter
    private String nickname;

    // 아이디 암호화. ex) abcdefg -> abcd****
    public void setId(String id){
        this.id = Optional.ofNullable(id)
            .map(str -> str.length() > 4 ? str.substring(0, 4) + "****" : str)
            .orElse(null);
    }

    public UserInfoDTO(Boolean message, String id, String nickname) {
        super(message);
        this.id = Optional.ofNullable(id)
            .map(str -> str.length() > 4 ? str.substring(0, 4) + "****" : str)
            .orElse(null);
        this.nickname = nickname;
    }
}
