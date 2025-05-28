package project.spring_basic.service.SessionServiceTest.unit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpSession;

import jakarta.servlet.http.HttpSession;
import project.spring_basic.data.dto.Request.NewAccountDTO;
import project.spring_basic.exception.DtoNullException;
import project.spring_basic.service.SessionServiceTest.SessionServiceUnitTestSupport;


@Tag("unit")
@Tag("service")
@Tag("service-unit")
public class UpdateSessionTest extends SessionServiceUnitTestSupport {

    @Test
    @DisplayName("세션에 담겨있는 데이터를 갱신한다.")
    public void updateSession() throws Exception {
        // given
        HttpSession session = new MockHttpSession();

        session.setAttribute("id", 1L);
        session.setAttribute("userId", "tttttttt");
        session.setAttribute("nickname", "테스트용 임시 계정");

        NewAccountDTO newAccountDTO = new NewAccountDTO(
                                            "tttttttt2",
                                            "테스트용 임시 계정2",
                                            "ttt@ttt.com",
                                            "000-0000-0000"
                                        );

        // when
        sessionService.updateSession(session, newAccountDTO);

        // then
        Long id = (Long) session.getAttribute("id");
        String userId = (String) session.getAttribute("userId");
        String nickname= (String) session.getAttribute("nickname");
        
        assertThat(id).isEqualTo(1L);
        assertThat(userId).isEqualTo("tttttttt2");
        assertThat(nickname).isEqualTo("테스트용 임시 계정2");
    }



    @Test
    @DisplayName("DTO가 존재하지 않을 경우에는 예외를 발생시킨다.")
    public void updateSessionDtoNullException() throws Exception {
        // given
        HttpSession session = new MockHttpSession();

        // when & then
        assertThatThrownBy(() -> sessionService.updateSession(session, null))
                    .isInstanceOf(DtoNullException.class)
                    .hasMessage("DTO가 존재하지 않습니다.");
    }
}
