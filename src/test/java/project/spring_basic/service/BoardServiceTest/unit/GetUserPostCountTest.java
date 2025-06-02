package project.spring_basic.service.BoardServiceTest.unit;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import project.spring_basic.exception.MemberNotFoundException;
import project.spring_basic.service.BoardServiceTest.BoardServiceUnitTestSupport;

@Tag("unit")
@Tag("service")
@Tag("service-unit")
@Tag("BoardService")
@Tag("BoardService-unit")
public class GetUserPostCountTest extends BoardServiceUnitTestSupport {

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
