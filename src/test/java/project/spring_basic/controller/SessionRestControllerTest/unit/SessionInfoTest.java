package project.spring_basic.controller.SessionRestControllerTest.unit;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
import project.spring_basic.data.dto.Response.Json.UserInfoDTO;

@Tag("unit")
@Tag("controller")
@Tag("controller-unit")
public class SessionInfoTest extends SessionRestControllerUnitTestSupport {

    @Test
    @DisplayName("현재 세션에 담겨있는 사용자의 문자열 아이디와 닉네임을 응답한다.")
    public void sessionInfo() throws Exception {
        // given
        MockHttpSession session = new MockHttpSession();

        UserInfoDTO newDto = new UserInfoDTO(true, "tttttttt", "테스트용 임시 계정");
        ApiResponse<UserInfoDTO> apiResponse = new ApiResponse<UserInfoDTO>(HttpStatus.OK, null, newDto);

        when(sessionService.getUserInfo(Mockito.any(UserInfoDTO.class), Mockito.eq(session)))
                .thenReturn(newDto);
        String ResponseJson = objectMapper.writeValueAsString(apiResponse);

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
        ApiResponse<ErrorDTO> apiResponse = new ApiResponse<ErrorDTO>(HttpStatus.INTERNAL_SERVER_ERROR, null, errorDTO);
        String ResponseJson = objectMapper.writeValueAsString(apiResponse);


        // when & then
        mockMvc.perform(get("/session").session(session))
                .andExpect(status().isInternalServerError())
                .andExpect(content().json(ResponseJson));
    } 
}
