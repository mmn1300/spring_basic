package project.spring_basic.controller.SessionRestControllerTest;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import project.spring_basic.api.controller.SessionRestController;
import project.spring_basic.data.dto.Response.Json.ErrorDTO;
import project.spring_basic.data.dto.Response.Json.UserInfoDTO;
import project.spring_basic.service.SessionService;

@Tag("unit")
@Tag("controller")
@Tag("controller-unit")
@WebMvcTest(controllers = SessionRestController.class)
public class SessionInfoTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private SessionService sessionService;



    @Test
    @DisplayName("현재 세션에 담겨있는 사용자의 문자열 아이디와 닉네임을 응답한다.")
    public void sessionInfo() throws Exception {
        // given
        MockHttpSession session = new MockHttpSession();

        UserInfoDTO newDto = new UserInfoDTO(true, "tttttttt", "테스트용 임시 계정");

        when(sessionService.getUserInfo(Mockito.any(UserInfoDTO.class), Mockito.eq(session)))
                .thenReturn(newDto);
        String ResponseJson = objectMapper.writeValueAsString(newDto);

        // when & then
        mockMvc.perform(get("/session").session(session))
                .andExpect(status().isOk())
                .andExpect(content().json(ResponseJson));
    }  



    @Test
    @DisplayName("처리 중 오류가 발생하면 {message:false, error:에러 메세지}를 응답한다.")
    public void sessionInfoWhenExceptionOccurs() throws Exception {
        // given
        MockHttpSession session = new MockHttpSession();

        when(sessionService.getUserInfo(Mockito.any(UserInfoDTO.class), Mockito.eq(session)))
                .thenThrow(new RuntimeException("처리 중 오류가 발생했습니다."));
        ErrorDTO errorDTO = new ErrorDTO(false, "처리 중 오류가 발생했습니다.");
        String ResponseJson = objectMapper.writeValueAsString(errorDTO);

        // when & then
        mockMvc.perform(get("/session").session(session))
                .andExpect(status().isOk())
                .andExpect(content().json(ResponseJson));
    } 
}
