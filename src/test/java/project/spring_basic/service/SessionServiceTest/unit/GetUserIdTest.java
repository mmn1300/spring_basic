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
public class GetUserIdTest extends SessionServiceUnitTestSupport {

    @Test
    @DisplayName("세션에 담겨있는 문자열 id 값을 반환한다.")
    public void getUserId() throws Exception {
        // given
        HttpSession session = new MockHttpSession();

        session.setAttribute("userId", "tttttttt");

        // when
        String result = sessionService.getUserId(session);

        // then
        assertThat(result).isEqualTo("tttttttt");
    }


    @Test
    @DisplayName("세션이 존재하지 않는 경우 공백문자열을 반환한다.")
    public void getUserIdWhenDoesSessionNotExist() throws Exception {
        // given
        HttpSession session = new MockHttpSession();

        // when
        String result = sessionService.getUserId(session);

        // then
        assertThat(result).isEqualTo("");
    }
}
