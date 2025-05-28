package project.spring_basic.controller.SessionRestControllerTest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import project.spring_basic.api.controller.SessionRestController;
import project.spring_basic.service.SessionService;

@WebMvcTest(controllers = SessionRestController.class)
public abstract class SessionRestControllerUnitTestSupport {
        
    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @MockitoBean
    protected SessionService sessionService;
}
