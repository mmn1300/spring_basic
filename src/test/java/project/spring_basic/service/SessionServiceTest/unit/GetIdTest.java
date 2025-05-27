package project.spring_basic.service.SessionServiceTest.unit;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpSession;

import jakarta.servlet.http.HttpSession;


@Tag("unit")
@Tag("service")
@Tag("service-unit")
public class GetIdTest extends SessionServiceUnitTestSupport {

    @Test
    @DisplayName("세션에 담겨있는 id 값을 반환한다.")
    public void getId() throws Exception {
        // given
        HttpSession session = new MockHttpSession();

        session.setAttribute("id", 1L);

        // when
        Long result = sessionService.getId(session);

        // then
        assertThat(result).isEqualTo(1L);
    }


    @Test
    @DisplayName("세션이 존재하지 않는 경우 -1L을 반환한다.")
    public void getIdWhenDoesSessionNotExist() throws Exception {
        // given
        HttpSession session = new MockHttpSession();

        // when
        Long result = sessionService.getId(session);

        // then
        assertThat(result).isEqualTo(-1L);
    }
}
