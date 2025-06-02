package project.spring_basic.service.MemberServiceTest;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import project.spring_basic.service.commands.MemberServiceCommands;
import project.spring_basic.service.imp.MemberServiceImp;
import project.spring_basic.service.querys.MemberServiceQuerys;

@ExtendWith(MockitoExtension.class)
public abstract class MemberServiceUnitTestSupport {
        
    @Mock
    protected MemberServiceCommands memberServiceCommands;

    @Mock
    protected MemberServiceQuerys memberServiceQuerys;

    @InjectMocks
    protected MemberServiceImp memberService;
}
