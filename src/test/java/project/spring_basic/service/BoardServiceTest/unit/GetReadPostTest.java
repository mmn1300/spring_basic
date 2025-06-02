package project.spring_basic.service.BoardServiceTest.unit;


import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import project.spring_basic.exception.PostNotFoundException;
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
public class GetReadPostTest {
                
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
    public void getReadPostArgumentException() throws Exception {
        assertThatThrownBy(() -> boardService.getReadPost(0L))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("양의 정수를 입력해야 합니다.");
    }



    @Test
    @DisplayName("존재하지 않는 게시물에 대한 메소드 실행에는 예외를 발생시킨다.")
    public void getReadPostException() throws Exception {
        // given
        Mockito.doThrow(new PostNotFoundException("1번 게시글은 존재하지 않습니다."))
            .when(boardServiceQuerys)
            .getPost(eq(1L));

        // when & then
        assertThatThrownBy(() -> boardService.getReadPost(1L))
                    .isInstanceOf(PostNotFoundException.class)
                    .hasMessage("1번 게시글은 존재하지 않습니다.");
    }

}
