package project.spring_basic.controller.ViewControllerTest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import project.spring_basic.api.controller.ViewController;
import project.spring_basic.service.SessionService;

@WebMvcTest(controllers = ViewController.class)
public abstract class ViewControllerUnitTestSupport {
        
    @Autowired
    protected MockMvc mockMvc;

    @MockitoBean
    protected SessionService sessionService;
}
