package project.spring_basic.service.SessionServiceTest.unit;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpSession;

import jakarta.servlet.http.HttpSession;
import project.spring_basic.service.SessionServiceTest.SessionServiceUnitTestSupport;


@Tag("unit")
@Tag("service")
@Tag("service-unit")
public class GetNicknameTest extends SessionServiceUnitTestSupport {

    @Test
    @DisplayName("세션에 담겨있는 nickname 값을 반환한다.")
    public void getNickname() throws Exception {
        // given
        HttpSession session = new MockHttpSession();

        session.setAttribute("nickname", "테스트용 임시 계정");

        // when
        String result = sessionService.getNickname(session);

        // then
        assertThat(result).isEqualTo("테스트용 임시 계정");
    }


    @Test
    @DisplayName("세션이 존재하지 않는 경우 공백문자열을 반환한다.")
    public void getNicknameWhenDoesSessionNotExist() throws Exception {
        // given
        HttpSession session = new MockHttpSession();

        // when
        String result = sessionService.getNickname(session);

        // then
        assertThat(result).isEqualTo("");
    }
}
