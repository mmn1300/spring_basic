package project.spring_basic.service.BoardServiceTest;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import project.spring_basic.service.commands.BoardServiceCommands;
import project.spring_basic.service.imp.BoardServiceImp;
import project.spring_basic.service.querys.BoardServiceQuerys;
import project.spring_basic.service.querys.MemberServiceQuerys;

@ExtendWith(MockitoExtension.class)
public abstract class BoardServiceUnitTestSupport {
        
    @Mock
    protected BoardServiceCommands boardServiceCommands;

    @Mock
    protected BoardServiceQuerys boardServiceQuerys;

    @Mock
    protected MemberServiceQuerys memberServiceQuerys;

    @InjectMocks
    protected BoardServiceImp boardService;
}
