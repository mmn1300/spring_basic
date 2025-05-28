package project.spring_basic.controller.BoardRestControllerTest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import project.spring_basic.api.controller.BoardRestController;
import project.spring_basic.service.BoardService;
import project.spring_basic.service.SessionService;

@WebMvcTest(controllers = BoardRestController.class)
public abstract class BoardRestControllerUnitTestSupport {
    
    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @MockitoBean
    protected SessionService sessionService;

    @MockitoBean
    protected BoardService boardService;
}
