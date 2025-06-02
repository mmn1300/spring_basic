package project.spring_basic.service.BoardServiceTest.unit;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import project.spring_basic.exception.PostNotFoundException;
import project.spring_basic.service.BoardServiceTest.BoardServiceUnitTestSupport;

@Tag("unit")
@Tag("service")
@Tag("service-unit")
@Tag("BoardService")
@Tag("BoardService-unit")
public class CheckUserTest extends BoardServiceUnitTestSupport {

    @Test
    @DisplayName("존재하지 않는 게시물에 대한 메소드 실행에는 예외를 발생시킨다.")
    public void getFileException() throws Exception {
        // given
        Mockito.doThrow(new PostNotFoundException("2번 게시글은 존재하지 않습니다."))
            .when(boardServiceQuerys)
            .getPost(eq(2L));

        // when & then
        assertThatThrownBy(() -> boardService.getFile(2L))
                    .isInstanceOf(PostNotFoundException.class)
                    .hasMessage("2번 게시글은 존재하지 않습니다.");
    }

}
