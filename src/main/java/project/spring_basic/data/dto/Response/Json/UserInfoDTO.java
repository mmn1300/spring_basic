package project.spring_basic.data.dto.Response.Json;

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
        String str = id;
        if(id.length() > 4){
            str = id.substring(0, 4) + "****";
        }
        this.id = str;
    }

    public UserInfoDTO(Boolean message, String id, String nickname) {
        super(message);
        this.id = id;
        this.nickname = nickname;
    }
}
