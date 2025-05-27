package project.spring_basic.service.SessionServiceTest.unit;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpSession;

import jakarta.servlet.http.HttpSession;
import project.spring_basic.data.dto.Response.Json.UserInfoDTO;


@Tag("unit")
@Tag("service")
@Tag("service-unit")
public class GetUserInfoTest extends SessionServiceUnitTestSupport {

    @Test
    @DisplayName("세션값을 기반으로 UserInfoDTO에 데이터를 담아 반환한다.")
    public void getUserInfo() throws Exception {
        // given
        HttpSession session = new MockHttpSession();

        session.setAttribute("id", 1L);
        session.setAttribute("userId", "tttttttt");
        session.setAttribute("nickname", "테스트용 임시 계정");

        UserInfoDTO userInfoDTO = new UserInfoDTO(null, null, null);

        // when
        userInfoDTO = sessionService.getUserInfo(userInfoDTO, session);

        // given
        assertThat(userInfoDTO).isNotNull()
                    .extracting("message", "id", "nickname")
                    .contains(true, "tttt****", "테스트용 임시 계정");
    }


    @Test
    @DisplayName("세션값이 존재하지 않는 경우 message값에 false를 담아 반환한다.")
    public void getUserInfoWhenDoesSessionNotExist() throws Exception {
        // given
        HttpSession session = new MockHttpSession();

        UserInfoDTO userInfoDTO = new UserInfoDTO(null, null, null);

        // when
        userInfoDTO = sessionService.getUserInfo(userInfoDTO, session);

        // given
        assertThat(userInfoDTO).isNotNull()
                    .extracting("message", "id", "nickname")
                    .contains(false, null, null);
    }

}
