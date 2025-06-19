package project.spring_basic.controller.SessionRestControllerTest.unit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MvcResult;

import jakarta.servlet.http.HttpSession;
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
        MockMultipartFile file = new MockMultipartFile("file", "test.txt", "text/plain", "test content".getBytes());


        // when
        MvcResult result = mockMvc.perform(delete("/session")
                .session(session)
                .with(csrf())
            )
            .andExpect(status().isOk())
            .andExpect(content().json(ResponseJson))
            .andReturn();

        // 인증 정보가 초기화 되었음을 검증한다
        mockMvc.perform(multipart("/board/post")
                        .file(file)
                        .session(session)
                        .param("title", "Test Title")
                        .param("content", "Test Content")
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .with(csrf())
                )
            .andExpect(status().isUnauthorized());


        // then
        // deleteAllSession() 호출 횟수 검증
        verify(sessionService, times(1))
            .deleteAllSession(any(HttpSession.class));

        MockHttpServletResponse response = result.getResponse();
        String setCookieHeader = response.getHeader("Set-Cookie");

        // 쿠키 삭제 검증
        assertThat(setCookieHeader).isNotNull()
            .contains("JSESSIONID=")
            .contains("Max-Age=0")
            .contains("Path=/");
    }



    @Test
    @DisplayName("CSRF 토큰이 존재하지 않는 요청이면 403에러를 응답한다.")
    public void logoutCsrfTokenNotExists() throws Exception {
        // given
        MockHttpSession session = new MockHttpSession();

        Mockito.doNothing().when(sessionService).deleteAllSession(session);
        ResponseDTO responseDTO = new ResponseDTO(false);
        ApiResponse<ResponseDTO> apiResponse = new ApiResponse<ResponseDTO>(HttpStatus.FORBIDDEN, null, responseDTO);
        String ResponseJson = objectMapper.writeValueAsString(apiResponse);


        // when
        mockMvc.perform(delete("/session").session(session))
            .andExpect(status().isForbidden())
            .andExpect(content().json(ResponseJson))
            .andReturn();
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
        mockMvc.perform(delete("/session")
                .session(session)
                .with(csrf())
            )
                .andExpect(status().isInternalServerError())
                .andExpect(content().json(ResponseJson));
    } 

}
