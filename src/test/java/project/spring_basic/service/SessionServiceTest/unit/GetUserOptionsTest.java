package project.spring_basic.service.SessionServiceTest.unit;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpSession;

import jakarta.servlet.http.HttpSession;
import project.spring_basic.data.dto.Response.ModelAttribute.OptionDTO;
import project.spring_basic.service.SessionServiceTest.SessionServiceUnitTestSupport;

@Tag("unit")
@Tag("service")
@Tag("service-unit")
public class GetUserOptionsTest extends SessionServiceUnitTestSupport {

    @Test
    @DisplayName("세션에 담겨있는 데이터를 기반으로 옵션 정보를 OptionDTO에 담아 반환한다.")
    public void getUserOptions() throws Exception {
        // given
        HttpSession session = new MockHttpSession();

        session.setAttribute("id", 1L);
        session.setAttribute("userId", "tttttttt");
        session.setAttribute("nickname", "테스트용 임시 계정");

        // when
        OptionDTO optionDTO = sessionService.getUserOptions(session, 1L);

        // then
        assertThat(optionDTO).isNotNull();
        assertThat(optionDTO.getUserOption()).isNotNull()
                .extracting("userId", "id")
                .contains("tttt****", 1L);
    }
}
