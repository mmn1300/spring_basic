package project.spring_basic.controller.ViewControllerTest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import project.spring_basic.api.controller.ViewController;
import project.spring_basic.api.custom.CustomEntryPoint.CustomAuthErrorEntryPoint;
import project.spring_basic.api.custom.CustomHandler.CustomAccessDeniedHandler;
import project.spring_basic.api.custom.CustomHandler.CustomAuthSuccessHandler;
import project.spring_basic.config.SecurityConfig;
import project.spring_basic.service.SessionService;

@WebMvcTest(controllers = ViewController.class)
@Import({SecurityConfig.class, CustomAuthErrorEntryPoint.class, CustomAccessDeniedHandler.class})
public abstract class ViewControllerUnitTestSupport {
        
    @Autowired
    protected MockMvc mockMvc;

    @MockitoBean
    protected SessionService sessionService;

    @MockitoBean
    protected CustomAuthSuccessHandler customAuthSuccessHandler;
}
