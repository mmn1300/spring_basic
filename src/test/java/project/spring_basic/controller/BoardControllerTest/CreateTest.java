package project.spring_basic.controller.BoardControllerTest;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import project.spring_basic.api.controller.BoardController;
import project.spring_basic.service.BoardService;
import project.spring_basic.service.SessionService;

@Tag("unit")
@Tag("controller")
@Tag("controller-unit")
@WebMvcTest(controllers = BoardController.class)
public class CreateTest {

    @Autowired
    private MockMvc mockMvc;
    
    @MockitoBean
    private SessionService sessionService;

    @MockitoBean
    private BoardService boardService;


    @Test
    @DisplayName("세션이 존재하면 write_post 템플릿을 응답한다.")
    public void create() throws Exception {
        // given
        MockHttpSession session = new MockHttpSession();

        when(sessionService.getTemplateOrDefault(session, "write_post")).thenReturn("write_post");

        // when & then
        mockMvc.perform(get("/board/create").session(session))
                .andExpect(status().isOk())
                .andExpect(view().name("write_post"));
    }


    @Test
    @DisplayName("세션이 존재하지 않으면 login으로 리다이렉트 시킨다.")
    public void createDoesSessionNotExist() throws Exception {
        // given
        MockHttpSession session = new MockHttpSession();

        when(sessionService.getTemplateOrDefault(session, "write_post"))
                .thenReturn("redirect:/login");

        // when & then
        mockMvc.perform(get("/board/create").session(session))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));
    }
}
