package project.spring_basic.data.Response.Json;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import project.spring_basic.data.dto.Response.Json.UserInfoDTO;

@Tag("unit")
@SpringBootTest
public class UserInfoDTOTest {
    
    @Test
    @DisplayName("생성자에서 아이디 값을 암호화한다.")
    public void encryptIdInConstructor() {
        // given
        Boolean message = true;
        String id = "a1b2c3d4";
        String nickname = "테스트 닉네임";

        String encId = "a1b2****";

        // when
        UserInfoDTO userInfoDTO = new UserInfoDTO(message, id, nickname);

        // then
        assertThat(userInfoDTO.getId()).isEqualTo(encId);
    }



    @Test
    @DisplayName("Setter에서 아이디 값을 암호화한다.")
    public void encryptIdInSetter() {
        // given
        Boolean message = true;
        String id = "a1b2c3d4";
        String nickname = "테스트 닉네임";

        String encId = "a1b2****";

        // when
        UserInfoDTO userInfoDTO = new UserInfoDTO(message, null, nickname);
        userInfoDTO.setId(id);

        // then
        assertThat(userInfoDTO.getId()).isEqualTo(encId);
    }
}
