package project.spring_basic.service.BoardServiceTest.unit;


import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import project.spring_basic.data.dto.Request.PostDTO;
import project.spring_basic.exception.DtoNullException;
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
public class SaveTest {
                
    @Mock
    private BoardServiceCommands boardServiceCommands;

    @Mock
    private BoardServiceQuerys boardServiceQuerys;

    @Mock
    private MemberServiceQuerys memberServiceQuerys;

    @InjectMocks
    private BoardServiceImp boardService;


    
    
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
