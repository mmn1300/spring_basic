package project.spring_basic.service.BoardServiceTest.unit;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import project.spring_basic.service.BoardServiceTest.BoardServiceUnitTestSupport;

@Tag("unit")
@Tag("service")
@Tag("service-unit")
@Tag("BoardService")
@Tag("BoardService-unit")
public class GetPostsInfoByUserTest extends BoardServiceUnitTestSupport {

    @Test
    @DisplayName("유효하지 않은 입력에 대한 예외를 발생시킨다.")
    public void getPostsInfoByUserException() throws Exception {
        assertThatThrownBy(() -> boardService.getPostsInfoByUser(0, 1L))
                    .isInstanceOf(IllegalArgumentException.class);

        assertThatThrownBy(() -> boardService.getPostsInfoByUser(1, 0L))
                    .isInstanceOf(IllegalArgumentException.class);
    }
}
