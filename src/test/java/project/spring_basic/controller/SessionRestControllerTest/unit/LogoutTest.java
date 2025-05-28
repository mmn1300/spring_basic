package project.spring_basic.controller.SessionRestControllerTest.unit;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpSession;

import project.spring_basic.api.ApiResponse;
import project.spring_basic.controller.SessionRestControllerTest.SessionRestControllerUnitTestSupport;
import project.spring_basic.data.dto.Response.Json.ErrorDTO;
import project.spring_basic.data.dto.Response.Json.ResponseDTO;

@Tag("unit")
@Tag("controller")
@Tag("controller-unit")
public class LogoutTest extends SessionRestControllerUnitTestSupport {

    @Test
    @DisplayName("현재 세션에 담겨있는 사용자의 정보를 모두 제거한 후 정상 처리되었는지를 응답한다.")
    public void logout() throws Exception {
        // given
        MockHttpSession session = new MockHttpSession();

        Mockito.doNothing().when(sessionService).deleteAllSession(session);
        ResponseDTO responseDTO = new ResponseDTO(true);
        ApiResponse<ResponseDTO> apiResponse = new ApiResponse<ResponseDTO>(HttpStatus.OK, null, responseDTO);
        String ResponseJson = objectMapper.writeValueAsString(apiResponse);

        // when & then
        mockMvc.perform(delete("/session").session(session))
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
        ApiResponse<ErrorDTO> apiResponse = new ApiResponse<ErrorDTO>(HttpStatus.INTERNAL_SERVER_ERROR, null, errorDTO);
        String ResponseJson = objectMapper.writeValueAsString(apiResponse);

        // when & then
        mockMvc.perform(delete("/session").session(session))
                .andExpect(status().isInternalServerError())
                .andExpect(content().json(ResponseJson));
    } 

}
