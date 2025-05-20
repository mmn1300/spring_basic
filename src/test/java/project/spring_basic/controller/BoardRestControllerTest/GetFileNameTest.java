package project.spring_basic.controller.BoardRestControllerTest;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import project.spring_basic.controller.BoardRestController;
import project.spring_basic.data.dto.Response.Json.ErrorDTO;
import project.spring_basic.data.dto.Response.Json.FileNameDTO;
import project.spring_basic.exception.PostNotFoundException;
import project.spring_basic.service.BoardService;
import project.spring_basic.service.SessionService;

@Tag("unit")
@WebMvcTest(controllers = BoardRestController.class)
public class GetFileNameTest {
        
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private SessionService sessionService;

    @MockitoBean
    private BoardService boardService;



    @Test
    @DisplayName("게시글에 첨부된 파일 이름을 응답한다.")
    public void getFileName() throws Exception {
        // given
        when(boardService.isFileExists(1L)).thenReturn("test.txt");

        FileNameDTO fileNameDTO = new FileNameDTO(true, "test.txt");
        String ResponseJson = objectMapper.writeValueAsString(fileNameDTO); // 객체 -> json 문자열


        // when & then
        mockMvc.perform(get("/board/file/1"))
                .andExpect(status().isOk())
                .andExpect(content().json(ResponseJson));
    }



    @Test
    @DisplayName("처리 중 오류가 발생하면 {message:false, error:에러 메세지}를 응답한다.")
    public void getFileNameWhenExceptionOccurs() throws Exception {
        // given
        when(boardService.isFileExists(1L))
            .thenThrow(new PostNotFoundException("1번 게시글은 존재하지 않습니다."));

        ErrorDTO errorDTO = new ErrorDTO(false, "1번 게시글은 존재하지 않습니다.");
        String ResponseJson = objectMapper.writeValueAsString(errorDTO); // 객체 -> json 문자열


        // when & then
        mockMvc.perform(get("/board/file/1"))
                .andExpect(status().isOk())
                .andExpect(content().json(ResponseJson));
    }
}