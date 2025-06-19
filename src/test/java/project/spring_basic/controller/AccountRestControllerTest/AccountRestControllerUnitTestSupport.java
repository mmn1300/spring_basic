package project.spring_basic.controller.AccountRestControllerTest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import project.spring_basic.api.controller.AccountRestController;
import project.spring_basic.api.custom.CustomEntryPoint.CustomAuthErrorEntryPoint;
import project.spring_basic.api.custom.CustomHandler.CustomAccessDeniedHandler;
import project.spring_basic.api.custom.CustomHandler.CustomAuthSuccessHandler;
import project.spring_basic.config.SecurityConfig;
import project.spring_basic.service.MemberService;
import project.spring_basic.service.SessionService;

@WebMvcTest(controllers = AccountRestController.class)
@Import({SecurityConfig.class, CustomAuthErrorEntryPoint.class, CustomAccessDeniedHandler.class})
public class AccountRestControllerUnitTestSupport {
        
    @Autowired
    protected MockMvc mockMvc;

    @MockitoBean
    protected MemberService memberService;

    @MockitoBean
    protected SessionService sessionService;

    @MockitoBean
    protected CustomAuthSuccessHandler customAuthSuccessHandler;
}
