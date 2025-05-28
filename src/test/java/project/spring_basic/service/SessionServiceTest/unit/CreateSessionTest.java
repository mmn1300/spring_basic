package project.spring_basic.service.SessionServiceTest.unit;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpSession;

import jakarta.servlet.http.HttpSession;
import project.spring_basic.data.entity.Member;
import project.spring_basic.service.SessionServiceTest.SessionServiceUnitTestSupport;


@Tag("unit")
@Tag("service")
@Tag("service-unit")
public class CreateSessionTest extends SessionServiceUnitTestSupport {

    @Test
    @DisplayName("회원 정보를 기반으로 세션을 생성한다.")
    public void createSession() throws Exception {
        // given
        HttpSession session = new MockHttpSession();

        Member member = Member.builder()
                .userId("tttttttt")
                .password("tttttttt")
                .nickname("테스트용 임시 계정")
                .email("ttt@ttt.com")
                .phoneNumber("000-0000-0000")
                .createAt(LocalDateTime.now())
                .level(1)
                .build();
        member.setId(1L);

        // when
        sessionService.createSession(session, member);

        // then
        assertThat(session.getAttribute("id")).isEqualTo(1L);
        assertThat(session.getAttribute("userId")).isEqualTo("tttttttt");
        assertThat(session.getAttribute("nickname")).isEqualTo("테스트용 임시 계정");
    }


    @Test
    @DisplayName(
        "회원 정보를 기반으로 세션을 생성한다." + 
        "다른 회원 정보로 메소드를 다시 실행해도 기존 세션값은 변하지 않는다."
        )
    public void createSession2() throws Exception {
        // given
        HttpSession session = new MockHttpSession();

        Member member = Member.builder()
                .userId("tttttttt")
                .password("tttttttt")
                .nickname("테스트용 임시 계정")
                .email("ttt@ttt.com")
                .phoneNumber("000-0000-0000")
                .createAt(LocalDateTime.now())
                .level(1)
                .build();
        member.setId(1L);

        Member newMember = Member.builder()
                .userId("tttttttt2")
                .password("tttttttt2")
                .nickname("테스트용 임시 계정2")
                .email("ttt@ttt.com")
                .phoneNumber("000-0000-0000")
                .createAt(LocalDateTime.now())
                .level(1)
                .build();
        newMember.setId(2L);

        // when
        sessionService.createSession(session, member);
        sessionService.createSession(session, newMember);

        // then
        assertThat(session.getAttribute("id")).isEqualTo(1L);
        assertThat(session.getAttribute("userId")).isEqualTo("tttttttt");
        assertThat(session.getAttribute("nickname")).isEqualTo("테스트용 임시 계정");
    }
}
