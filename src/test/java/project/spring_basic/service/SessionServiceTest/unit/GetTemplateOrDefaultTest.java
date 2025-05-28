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
public class GetTemplateOrDefaultTest extends SessionServiceUnitTestSupport {

    @Test
    @DisplayName("세션이 존재하는 경우 입력된 view 데이터를 그대로 반환한다.")
    public void getTemplateOrDefault() throws Exception {
        // given
        HttpSession session = new MockHttpSession();

        session.setAttribute("id", 1L);

        String view = "index";

        // when
        String result = sessionService.getTemplateOrDefault(session, view);

        // then
        assertThat(result).isEqualTo(view);
    }


    @Test
    @DisplayName("세션이 존재하지 않는 경우 로그인 페이지 리다이렉트 데이터를 반환한다.")
    public void getTemplateOrDefaultWhenDoesSessionNotExist() throws Exception {
        // given
        HttpSession session = new MockHttpSession();

        String view = "index";

        // when
        String result = sessionService.getTemplateOrDefault(session, view);


        // then
        assertThat(result).isEqualTo("redirect:/login");
    }
}
