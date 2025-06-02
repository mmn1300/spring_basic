package project.spring_basic.service.BoardServiceTest.unit;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import project.spring_basic.exception.MemberNotFoundException;
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
public class GetUserPostCountTest {
    
    @Mock
    private BoardServiceCommands boardServiceCommands;

    @Mock
    private BoardServiceQuerys boardServiceQuerys;

    @Mock
    private MemberServiceQuerys memberServiceQuerys;

    @InjectMocks
    private BoardServiceImp boardService;


    @Test
    @DisplayName("존재하지 않는 회원에 대한 메소드 실행에는 예외를 발생시킨다.")
    public void getUserPostCountException() throws Exception {
        // given
        Mockito.doThrow(new MemberNotFoundException("해당 회원은 존재하지 않습니다."))
            .when(memberServiceQuerys)
            .getMemberByUserId(anyString());

        // when & then
        assertThatThrownBy(() -> memberServiceQuerys.getMemberByUserId("aaaaaaaa"))
            .isInstanceOf(MemberNotFoundException.class)
            .hasMessage("해당 회원은 존재하지 않습니다.");
    }
}
