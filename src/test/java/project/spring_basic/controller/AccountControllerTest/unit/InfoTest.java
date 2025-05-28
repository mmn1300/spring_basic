package project.spring_basic.controller.AccountControllerTest.unit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.servlet.FlashMap;

import project.spring_basic.controller.AccountControllerTest.AccountControllerUnitTestSupport;
import project.spring_basic.data.dto.Response.ModelAttribute.AccountInfoDTO;
import project.spring_basic.exception.MemberNotFoundException;


@Tag("unit")
@Tag("controller")
@Tag("controller-unit")
public class InfoTest extends AccountControllerUnitTestSupport {

    @Test
    @DisplayName("세션 정보를 기반으로 유저 계정 정보를 담아 my_page 템플릿과 함께 응답한다.")
    public void info() throws Exception {
        // given
        MockHttpSession session = new MockHttpSession();

        AccountInfoDTO accountInfoDTO = new AccountInfoDTO(
            1L, "tttttttt", "테스트용 임시 계정",
            "ttt@ttt.com", "000-0000-0000"
        );

        when(sessionService.getId(session)).thenReturn(1L);
        when(memberService.getAccountInfo(1L)).thenReturn(accountInfoDTO);
        when(sessionService.getTemplateOrDefault(session, "my_page")).thenReturn("my_page");


        // when & then
        mockMvc.perform(get("/account/info").session(session))
                .andExpect(status().isOk())
                .andExpect(view().name("my_page"))
                .andExpect(model().attribute("accountInfo", accountInfoDTO));
    }


    @Test
    @DisplayName("세션이 존재하지 않는 경우 login 화면으로 리다이렉트 시킨다.")
    public void infoWhenDoesSessionNotExist() throws Exception {
        // given
        MockHttpSession session = new MockHttpSession();

        when(sessionService.getId(session)).thenReturn(-1L);
        when(sessionService.getTemplateOrDefault(session, "my_page"))
                .thenReturn("redirect:/login");


        // when & then
        mockMvc.perform(get("/account/info").session(session))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));
    }


    @Test
    @DisplayName("처리 도중 예외가 발생하면 원인 메세지와 함께 에러 화면으로 리다이렉트 시킨다.")
    public void infoWhenExceptionOccurs() throws Exception {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("id", 1L);

        when(sessionService.getId(session)).thenReturn(1L);
        when(memberService.getAccountInfo(1L))
                .thenThrow(new MemberNotFoundException("해당 회원은 존재하지 않습니다."));


        // when & then
        MvcResult result = mockMvc.perform(get("/account/info")
                    .session(session)
                    .header("Accept", "text/html")
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/error"))
                .andReturn();

        FlashMap flashMap = result.getFlashMap();
        assertThat(flashMap.get("error")).isEqualTo("해당 회원은 존재하지 않습니다.");
    }
}
