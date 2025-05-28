package project.spring_basic.service.SessionServiceTest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import project.spring_basic.service.SessionService;

@ActiveProfiles("test")
@SpringBootTest
public abstract class SessionServiceUnitTestSupport {
    
    @Autowired
    protected SessionService sessionService;
}
