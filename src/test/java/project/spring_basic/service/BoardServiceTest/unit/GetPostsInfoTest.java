package project.spring_basic.service.BoardServiceTest.unit;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import project.spring_basic.service.commands.BoardServiceCommands;
import project.spring_basic.service.imp.BoardServiceImp;
import project.spring_basic.service.querys.BoardServiceQuerys;
import project.spring_basic.service.querys.MemberServiceQuerys;

@Tag("unit")
@Tag("service")
@Tag("service-unit")
@Tag("BoardService")
@Tag("BoardService-unit")
@ExtendWith(MockitoExtension.class)
public class GetPostsInfoTest {
            
    @Mock
    private BoardServiceCommands boardServiceCommands;

    @Mock
    private BoardServiceQuerys boardServiceQuerys;

    @Mock
    private MemberServiceQuerys memberServiceQuerys;

    @InjectMocks
    private BoardServiceImp boardService;



    @Test
    @DisplayName("유효하지 않은 입력에 대한 예외를 발생시킨다.")
    public void getPostsInfoException() throws Exception {
        assertThatThrownBy(() -> boardService.getPostsInfo(0))
                    .isInstanceOf(IllegalArgumentException.class);
    }
}
