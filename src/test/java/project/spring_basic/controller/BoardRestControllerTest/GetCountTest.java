package project.spring_basic.controller.BoardRestControllerTest;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

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
import project.spring_basic.exception.MemberNotFoundException;
import project.spring_basic.service.BoardService;
import project.spring_basic.service.SessionService;

@Tag("unit")
@WebMvcTest(controllers = BoardRestController.class)
public class GetCountTest {
        
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private SessionService sessionService;

    @MockitoBean
    private BoardService boardService;



    @Test
    @DisplayName("사용자가 작성한 총 게시글의 수와 정상 처리 유무를 응답한다.")
    public void getCount() throws Exception {
        // given
        when(boardService.getUserPostCount("tttttttt")).thenReturn(1);


        // when & then
        mockMvc.perform(get("/board/tttttttt/posts/count"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value(true))
                .andExpect(jsonPath("$.num").value(1));
    }



    @Test
    @DisplayName("처리 중 오류가 발생하면 {message:false, error:에러 메세지}를 응답한다.")
    public void getCountWhenExceptionOccurs() throws Exception {
        // given
        when(boardService.getUserPostCount("tttttttt"))
            .thenThrow(new MemberNotFoundException("해당 회원은 존재하지 않습니다."));

        ErrorDTO errorDTO = new ErrorDTO(false, "해당 회원은 존재하지 않습니다.");
        String ResponseJson = objectMapper.writeValueAsString(errorDTO); // 객체 -> json 문자열


        // when & then
        mockMvc.perform(get("/board/tttttttt/posts/count"))
                .andExpect(status().isOk())
                .andExpect(content().json(ResponseJson));
    }
}
