package project.spring_basic.controller.SessionRestControllerTest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
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
import project.spring_basic.data.dto.Response.Json.ResponseDTO;
import project.spring_basic.service.SessionService;

@Tag("unit")
@Tag("controller")
@Tag("controller-unit")
@WebMvcTest(controllers = SessionRestController.class)
public class LogoutTest {
    
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private SessionService sessionService;


    @Test
    @DisplayName("현재 세션에 담겨있는 사용자의 정보를 모두 제거한 후 정상 처리되었는지를 응답한다.")
    public void logout() throws Exception {
        // given
        MockHttpSession session = new MockHttpSession();

        Mockito.doNothing().when(sessionService).deleteAllSession(session);
        ResponseDTO responseDTO = new ResponseDTO(true);
        String ResponseJson = objectMapper.writeValueAsString(responseDTO);

        // when & then
        mockMvc.perform(delete("/session/logout").session(session))
                .andExpect(status().isOk())
                .andExpect(content().json(ResponseJson));
    }  



    @Test
    @DisplayName("처리 중 오류가 발생하면 {message:false, error:에러 메세지}를 응답한다.")
    public void logoutWhenExceptionOccurs() throws Exception {
        // given
        MockHttpSession session = new MockHttpSession();

        Mockito.doThrow(new RuntimeException("처리 중 오류가 발생했습니다."))
                .when(sessionService).deleteAllSession(session);
        ErrorDTO errorDTO = new ErrorDTO(false, "처리 중 오류가 발생했습니다.");
        String ResponseJson = objectMapper.writeValueAsString(errorDTO);

        // when & then
        mockMvc.perform(delete("/session/logout").session(session))
                .andExpect(status().isOk())
                .andExpect(content().json(ResponseJson));
    } 

}
