package project.spring_basic.service.BoardServiceTest.unit;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import project.spring_basic.data.dto.Request.PostDTO;
import project.spring_basic.exception.DtoNullException;
import project.spring_basic.exception.PostNotFoundException;
import project.spring_basic.service.BoardServiceTest.BoardServiceUnitTestSupport;

@Tag("unit")
@Tag("service")
@Tag("service-unit")
@Tag("BoardService")
@Tag("BoardService-unit")
@ExtendWith(MockitoExtension.class)
public class UpdateTest extends BoardServiceUnitTestSupport {

    @Test
    @DisplayName("존재하지 않는 게시물에 대한 메소드 실행에는 예외를 발생시킨다.")
    public void updatePostException() throws Exception {
        // given
        PostDTO postDTO = new PostDTO("1", "1");
        MultipartFile file = null;

        Mockito.doThrow(new PostNotFoundException("1번 게시글은 존재하지 않습니다."))
            .when(boardServiceQuerys)
            .getPost(eq(1L));


        // when & then
            assertThatThrownBy(() -> boardService.update(1L, postDTO, file))
                        .isInstanceOf(PostNotFoundException.class)
                        .hasMessage("1번 게시글은 존재하지 않습니다.");
        }



    // 존재하지 않는 데이터에 대한 예외 발생
    @Test
    @DisplayName("DTO가 존재하지 않을 경우에는 예외를 발생시킨다.")
    public void updateDtoNullException() throws Exception {
        assertThatThrownBy(() -> boardService.update(1L, null, null))
                    .isInstanceOf(DtoNullException.class)
                    .hasMessage("DTO가 존재하지 않습니다.");
    }



    // 유효하지 않는 입력값에 대한 예외 발생
    @Test
    @DisplayName("유효하지 않은 입력에는 예외를 발생시킨다.")
    public void updateDtoArgumentException() throws Exception {
        // given   
        PostDTO postDTO = new PostDTO("1", "1");

        // when & then
        assertThatThrownBy(() -> boardService.update(0L, postDTO, null))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("양의 정수를 입력해야 합니다.");
    }

}