package project.spring_basic.controller.BoardRestControllerTest.unit;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;

import org.hamcrest.Matchers;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;

import project.spring_basic.api.ApiResponse;
import project.spring_basic.controller.BoardRestControllerTest.BoardRestControllerUnitTestSupport;
import project.spring_basic.data.dto.Request.PostDTO;
import project.spring_basic.data.dto.Response.Json.ErrorDTO;

@Tag("unit")
@Tag("controller")
@Tag("controller-unit")
public class UpdatePostTest extends BoardRestControllerUnitTestSupport {

    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    @DisplayName("정상처리 유무를 응답한다.")
    public void updatePost() throws Exception {
        // given
        MockMultipartFile file = new MockMultipartFile("file", "test.txt", "text/plain", "test content".getBytes());
        MockHttpSession session = new MockHttpSession();

        Mockito.when(sessionService.getId(session)).thenReturn(1L);
        Mockito.doNothing().when(boardService)
                .update(Mockito.eq(1L), Mockito.any(PostDTO.class), Mockito.eq(file));


        // when & then
        mockMvc.perform(multipart("/board/post/1")
                        .file(file)
                        .with(request -> {
                                    request.setMethod("PUT"); // PUT으로 오버라이드
                                    return request;
                                })
                        .session(session)
                        .param("title", "Test Title")
                        .param("content", "Test Content")
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .with(csrf())
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.data.message").value(true));
    }



    @Test
    @DisplayName("인증되지 않은 상태로 요청하면 401에러를 응답한다.")
    public void updatePostNotAuthenticated() throws Exception {
        // given
        MockMultipartFile file = new MockMultipartFile("file", "test.txt", "text/plain", "test content".getBytes());
        MockHttpSession session = new MockHttpSession();

        Mockito.when(sessionService.getId(session)).thenReturn(1L);
        Mockito.doNothing().when(boardService)
                .update(Mockito.eq(1L), Mockito.any(PostDTO.class), Mockito.eq(file));


        // when & then
        mockMvc.perform(multipart("/board/post/1")
                        .file(file)
                        .with(request -> {
                                    request.setMethod("PUT"); // PUT으로 오버라이드
                                    return request;
                                })
                        .session(session)
                        .param("title", "Test Title")
                        .param("content", "Test Content")
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .with(csrf())
                )
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.code").value(401))
                .andExpect(jsonPath("$.status").value("UNAUTHORIZED"))
                .andExpect(jsonPath("$.data.message").value(false));
    }



    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    @DisplayName("CSRF 토큰이 존재하지 않는 요청이면 403에러를 응답한다.")
    public void updatePostCsrfTokenNotExists() throws Exception {
        // given
        MockMultipartFile file = new MockMultipartFile("file", "test.txt", "text/plain", "test content".getBytes());
        MockHttpSession session = new MockHttpSession();

        Mockito.when(sessionService.getId(session)).thenReturn(1L);
        Mockito.doNothing().when(boardService)
                .update(Mockito.eq(1L), Mockito.any(PostDTO.class), Mockito.eq(file));


        // when & then
        mockMvc.perform(multipart("/board/post/1")
                        .file(file)
                        .with(request -> {
                                    request.setMethod("PUT"); // PUT으로 오버라이드
                                    return request;
                                })
                        .session(session)
                        .param("title", "Test Title")
                        .param("content", "Test Content")
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                )
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.code").value(403))
                .andExpect(jsonPath("$.status").value("FORBIDDEN"))
                .andExpect(jsonPath("$.data.message").value(false));
    }



    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    @DisplayName("처리 중 오류가 발생하면 {message:false, error:에러 메세지}를 응답한다.")
    public void updatePostWhenExceptionOccurs() throws Exception {
        // given
        MockMultipartFile file = new MockMultipartFile("file", "test.txt", "text/plain", "test content".getBytes());
        MockHttpSession session = new MockHttpSession();

        Mockito.when(sessionService.getId(session)).thenReturn(1L);
        Mockito.doThrow(new RuntimeException("데이터베이스 접근 오류"))
                .when(boardService)
                .update(Mockito.eq(1L), Mockito.any(PostDTO.class), Mockito.eq(file));

        ErrorDTO errorDTO = new ErrorDTO(false, "데이터베이스 접근 오류");
        ApiResponse<ErrorDTO> apiResponse = new ApiResponse<ErrorDTO>(HttpStatus.INTERNAL_SERVER_ERROR, null, errorDTO);
        String ResponseJson = objectMapper.writeValueAsString(apiResponse); // 객체 -> json 문자열


        // when & then
        mockMvc.perform(multipart("/board/post/1")
                        .file(file)
                        .with(request -> {
                                    request.setMethod("PUT"); // PUT으로 오버라이드
                                    return request;
                                })
                        .session(session)
                        .param("title", "Test Title")
                        .param("content", "Test Content")
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .with(csrf())
                )
                .andExpect(status().isInternalServerError())
                .andExpect(content().json(ResponseJson));
    }



    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    @DisplayName("잘못된 입력 데이터로 요청하면 400번 코드를 응답한다.")
    public void updatePostWhenBadRequest() throws Exception {
        // given
        MockMultipartFile file = new MockMultipartFile("file", "test.txt", "text/plain", "test content".getBytes());
        MockHttpSession session = new MockHttpSession();


        // when & then
        mockMvc.perform(multipart("/board/post/1")
                        .file(file)
						.with(request -> {
                                    request.setMethod("PUT"); // PUT으로 오버라이드
                                    return request;
                                })
                        .session(session)
                        .param("title", "")
                        .param("content", "")
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .with(csrf())
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(400))
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.messages").isArray())
                .andExpect(jsonPath("$.messages").value(
                    Matchers.containsInAnyOrder(
                        "제목이 존재해야 합니다.",
                        "내용물이 존재해야 합니다.",
                        "제목은 1자 이상 200자 이하여야 합니다."
                    )
                ));
    }
}
