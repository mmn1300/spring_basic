package project.spring_basic.controller.AccountControllerTest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import project.spring_basic.api.controller.AccountController;
import project.spring_basic.service.MemberService;
import project.spring_basic.service.SessionService;

@WebMvcTest(controllers = AccountController.class)
public class AccountControllerUnitTestSupport {

    @Autowired
    protected MockMvc mockMvc;

    @MockitoBean
    protected MemberService memberService;

    @MockitoBean
    protected SessionService sessionService;
}
