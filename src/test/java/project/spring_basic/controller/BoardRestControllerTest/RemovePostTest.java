package project.spring_basic.controller.BoardRestControllerTest;

import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import project.spring_basic.controller.BoardRestController;
import project.spring_basic.data.dto.Response.Json.ErrorDTO;
import project.spring_basic.service.BoardService;
import project.spring_basic.service.SessionService;

@Tag("unit")
@WebMvcTest(controllers = BoardRestController.class)
public class RemovePostTest {
    
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
    public void removePost() throws Exception {
        // given
        Mockito.doNothing().when(boardService)
                .remove(eq(1L));


        // when & then
        mockMvc.perform(delete("/board/post/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value(true));
    }



    @Test
    @DisplayName("처리 중 오류가 발생하면 {message:false, error:에러 메세지}를 응답한다.")
    public void removePostWhenExceptionOccurs() throws Exception {
        // given
        Mockito.doThrow(new RuntimeException("데이터베이스 접근 오류"))
                .when(boardService)
                .remove(eq(1L));

        ErrorDTO errorDTO = new ErrorDTO(false, "데이터베이스 접근 오류");
        String ResponseJson = objectMapper.writeValueAsString(errorDTO); // 객체 -> json 문자열


        // when & then
        mockMvc.perform(delete("/board/post/1"))
                .andExpect(status().isOk())
                .andExpect(content().json(ResponseJson));
    }

}
