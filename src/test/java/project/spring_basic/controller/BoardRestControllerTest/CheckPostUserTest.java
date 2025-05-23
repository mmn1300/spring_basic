package project.spring_basic.controller.BoardRestControllerTest;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import project.spring_basic.api.controller.BoardRestController;
import project.spring_basic.data.dto.Response.Json.BooleanDTO;
import project.spring_basic.data.dto.Response.Json.ErrorDTO;
import project.spring_basic.exception.MemberNotFoundException;
import project.spring_basic.service.BoardService;
import project.spring_basic.service.SessionService;

@Tag("unit")
@Tag("controller")
@Tag("controller-unit")
@WebMvcTest(controllers = BoardRestController.class)
public class CheckPostUserTest {
        
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private SessionService sessionService;

    @MockitoBean
    private BoardService boardService;



    @Test
    @DisplayName("정상처리 유무와 게시글의 작성자 일치 여부를 응답한다.")
    public void checkPostUser() throws Exception {
        // given
        MockHttpSession session = new MockHttpSession();

        when(sessionService.getUserId(session)).thenReturn("tttttttt");
        when(boardService.checkUser(1L, "tttttttt")).thenReturn(true);

        BooleanDTO booleanDTO = new BooleanDTO(true, true);
        String ResponseJson = objectMapper.writeValueAsString(booleanDTO);


        // when & then
        mockMvc.perform(get("/board/user/1")
                    .session(session)
                )
                .andExpect(status().isOk())
                .andExpect(content().json(ResponseJson));
    }



    @Test
    @DisplayName("처리 중 오류가 발생하면 {message:false, error:에러 메세지}를 응답한다.")
    public void checkPostUserWhenExceptionOccurs() throws Exception {
        // given
        MockHttpSession session = new MockHttpSession();

        when(sessionService.getUserId(session)).thenReturn("tttttttt");
        when(boardService.checkUser(1L, "tttttttt"))
                .thenThrow(new MemberNotFoundException("해당 회원은 존재하지 않습니다."));

        ErrorDTO errorDTO = new ErrorDTO(false, "해당 회원은 존재하지 않습니다.");
        String ResponseJson = objectMapper.writeValueAsString(errorDTO);


        // when & then
        mockMvc.perform(get("/board/user/1")
                    .session(session)
                )
                .andExpect(status().isOk())
                .andExpect(content().json(ResponseJson));
    }
}
