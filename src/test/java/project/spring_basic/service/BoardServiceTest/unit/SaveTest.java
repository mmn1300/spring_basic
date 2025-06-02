package project.spring_basic.service.BoardServiceTest.unit;


import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import project.spring_basic.data.dto.Request.PostDTO;
import project.spring_basic.exception.DtoNullException;
import project.spring_basic.service.BoardServiceTest.BoardServiceUnitTestSupport;

@Tag("unit")
@Tag("service")
@Tag("service-unit")
@Tag("BoardService")
@Tag("BoardService-unit")
public class SaveTest extends BoardServiceUnitTestSupport {

    @Test
    @DisplayName("DTO가 존재하지 않을 경우에는 예외를 발생시킨다.")
    public void saveDtoNullException() throws Exception {
        assertThatThrownBy(() -> boardService.save(null, 1L, null))
                .isInstanceOf(DtoNullException.class)
                .hasMessage("DTO가 존재하지 않습니다.");
    }


    @Test
    @DisplayName("유효하지 않은 입력에는 예외를 발생시킨다.")
    public void saveArgumentException() throws Exception {
        // given
        PostDTO postDTO = new PostDTO("1", "1");

        // when & then
        assertThatThrownBy(() -> boardService.save(postDTO, 0L, null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("양의 정수를 입력해야 합니다.");
    }
}
