package project.spring_basic.controller.BoardRestControllerTest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import project.spring_basic.api.controller.BoardRestController;
import project.spring_basic.data.dto.Request.PostDTO;
import project.spring_basic.data.dto.Response.Json.ErrorDTO;
import project.spring_basic.service.BoardService;
import project.spring_basic.service.SessionService;

@Tag("unit")
@Tag("controller")
@Tag("controller-unit")
@WebMvcTest(controllers = BoardRestController.class)
public class StorePostTest {
    
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private SessionService sessionService;

    @MockitoBean
    private BoardService boardService;



    @Test
    @DisplayName("정상처리 유무를 응답한다.")
    public void storePost() throws Exception {
        // given
        MockMultipartFile file = new MockMultipartFile("file", "test.txt", "text/plain", "test content".getBytes());
        MockHttpSession session = new MockHttpSession();

        Mockito.when(sessionService.getId(session)).thenReturn(1L);
        Mockito.doNothing().when(boardService)
                .save(Mockito.any(PostDTO.class), Mockito.eq(1L), Mockito.eq(file));


        // when & then
        mockMvc.perform(multipart("/board/post")
                        .file(file)
                        .session(session)
                        .param("title", "Test Title")
                        .param("content", "Test Content")
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value(true));
    }



    @Test
    @DisplayName("처리 중 오류가 발생하면 {message:false, error:에러 메세지}를 응답한다.")
    public void storePostWhenExceptionOccurs() throws Exception {
        // given
        MockMultipartFile file = new MockMultipartFile("file", "test.txt", "text/plain", "test content".getBytes());
        MockHttpSession session = new MockHttpSession();

        Mockito.when(sessionService.getId(session)).thenReturn(1L);
        Mockito.doThrow(new RuntimeException("데이터베이스 접근 오류"))
                .when(boardService)
                .save(Mockito.any(PostDTO.class), Mockito.eq(1L), Mockito.eq(file));

        ErrorDTO errorDTO = new ErrorDTO(false, "데이터베이스 접근 오류");
        String ResponseJson = objectMapper.writeValueAsString(errorDTO); // 객체 -> json 문자열


        // when & then
        mockMvc.perform(multipart("/board/post")
                        .file(file)
                        .session(session)
                        .param("title", "Test Title")
                        .param("content", "Test Content")
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                )
                .andExpect(status().isOk())
                .andExpect(content().json(ResponseJson));
    }


    
    @Test
    @DisplayName("잘못된 입력 데이터로 요청하면 400번 코드를 응답한다.")
    public void storePostWhenBadRequest() throws Exception {
        // given
        MockMultipartFile file = new MockMultipartFile("file", "test.txt", "text/plain", "test content".getBytes());
        MockHttpSession session = new MockHttpSession();


        // when & then
        mockMvc.perform(multipart("/board/post")
                        .file(file)
                        .session(session)
                        .param("title", "")
                        .param("content", "")
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                )
                .andExpect(status().isBadRequest());
    }
}
