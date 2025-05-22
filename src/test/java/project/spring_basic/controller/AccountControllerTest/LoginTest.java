package project.spring_basic.controller.AccountControllerTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.servlet.FlashMap;

import project.spring_basic.controller.AccountController;
import project.spring_basic.data.entity.Member;
import project.spring_basic.exception.MemberNotFoundException;
import project.spring_basic.service.MemberService;
import project.spring_basic.service.SessionService;


@Tag("unit")
@WebMvcTest(controllers = AccountController.class)
public class LoginTest {
    
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private MemberService memberService;

    @MockitoBean
    private SessionService sessionService;



    @Test
    @DisplayName("로그인에 성공하면 세션을 생성하고 게시판으로 리다이렉트 시킨다.")
    public void login() throws Exception {
        // given
        MockHttpSession session = new MockHttpSession();

        Member member = null;
        when(memberService.getMemberByUserId("tttttttt")).thenReturn(member);

        Mockito.doNothing().when(sessionService).createSession(session, member);

        // when & then
        mockMvc.perform(post("/account/login")
                    .session(session)
                    .param("id", "tttttttt")
                    .param("pw", "tttttttt")
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/board"));
    }


    @Test
    @DisplayName("로그인에 실패하면 원인 메세지와 함께 에러 화면으로 리다이렉트 시킨다.")
    public void loginWhenExceptionOccurs() throws Exception {
        // given
        MockHttpSession session = new MockHttpSession();

        when(memberService.getMemberByUserId("tttttttt"))
                .thenThrow(new MemberNotFoundException("해당 회원은 존재하지 않습니다."));

        Mockito.doNothing().when(sessionService).createSession(session, null);

        // when & then
        MvcResult result = mockMvc.perform(post("/account/login")
                    .session(session)
                    .param("id", "tttttttt")
                    .param("pw", "tttttttt")
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/error"))
                .andReturn();

        FlashMap flashMap = result.getFlashMap();
        assertThat(flashMap.get("error")).isEqualTo("해당 회원은 존재하지 않습니다.");
    }


    // 잘못된 입력 데이터에 대한 응답
    @Test
    @DisplayName("잘못된 입력 데이터로 요청하면 400번 코드를 응답한다.")
    public void loginWhenBadRequest() throws Exception {
        // given
        MockHttpSession session = new MockHttpSession();

        // when & then
        mockMvc.perform(post("/account/login")
                    .session(session)
                    .param("id", "t")
                    .param("pw", "t")
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                )
                .andExpect(status().isBadRequest());
    }
}
