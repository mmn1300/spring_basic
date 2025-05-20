package project.spring_basic.controller.ViewControllerTest;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.hamcrest.Matchers.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import project.spring_basic.controller.ViewController;
import project.spring_basic.data.dto.Response.ModelAttribute.OptionDTO;
import project.spring_basic.service.SessionService;

@Tag("unit")
@WebMvcTest(controllers = ViewController.class)
public class BoardTest {
    
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private SessionService sessionService;


    @Test
    @DisplayName("사용자 파라미터가 없는 경우 옵션값을 비운채로 board 템플릿과 함께 응답한다.")
    public void boardTest() throws Exception {
        // given
        MockHttpSession session = new MockHttpSession();

        when(sessionService.getTemplateOrDefault(session, "board")).thenReturn("board");


        // when & then
        mockMvc.perform(get("/board").session(session))
                .andExpect(status().isOk())
                .andExpect(view().name("board"))
                .andExpect(model().attribute("option", hasProperty("userOption", allOf(
                        hasProperty("userId", is("")),
                        hasProperty("id", is(nullValue()))
                ))));

    }



    @Test
    @DisplayName("사용자 파라미터가 있는 경우 옵션값에 사용자 정보를 담아 board 템플릿과 함께 응답한다.")
    public void boardTestWithUserData() throws Exception {
        // given
        MockHttpSession session = new MockHttpSession();

        OptionDTO optionDTO = new OptionDTO("tttttttt", 1L);
        when(sessionService.getUserOptions(session, 1L)).thenReturn(optionDTO);
        when(sessionService.getTemplateOrDefault(session, "board")).thenReturn("board");


        // when & then
        mockMvc.perform(get("/board?user=1").session(session))
                .andExpect(status().isOk())
                .andExpect(view().name("board"))
                .andExpect(model().attribute("option", hasProperty("userOption", allOf(
                        hasProperty("userId", is("tttt****")),
                        hasProperty("id", is(1L))
                ))));
    }
}
