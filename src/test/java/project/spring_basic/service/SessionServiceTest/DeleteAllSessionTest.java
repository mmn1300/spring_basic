package project.spring_basic.service.SessionServiceTest;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.mock.web.MockHttpSession;

import jakarta.servlet.http.HttpSession;
import project.spring_basic.service.SessionService;


@Tag("unit")
@ActiveProfiles("test")
@SpringBootTest
public class DeleteAllSessionTest {
    
    @Autowired
    private SessionService sessionService;



    @Test
    @DisplayName("생성된 세션 값을 제거한다.")
    public void deleteAllSession() throws Exception {
        // given
        HttpSession session = new MockHttpSession();

        session.setAttribute("id", 1L);
        session.setAttribute("userId", "tttttttt");
        session.setAttribute("nickname", "테스트용 임시 계정");

        // when
        sessionService.deleteAllSession(session);

        // then
        assertThat(session.getAttribute("id")).isNull();
        assertThat(session.getAttribute("userId")).isNull();
        assertThat(session.getAttribute("nickname")).isNull();
    }
}
