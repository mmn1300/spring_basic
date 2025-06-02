package project.spring_basic.service.MemberServiceTest.unit;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import project.spring_basic.exception.MemberNotFoundException;
import project.spring_basic.service.MemberServiceTest.MemberServiceUnitTestSupport;

@Tag("integration")
@Tag("service")
@Tag("service-integration")
@Tag("MemberService")
@Tag("MemberService-integration")
public class GetAccountInfoTest extends MemberServiceUnitTestSupport {

    @Test
    @DisplayName("존재하지 않는 회원에 대한 메소드 실행에는 예외를 발생시킨다.")
    public void getAccountInfoMemberException() throws Exception {
        // given
        Mockito.doThrow(new MemberNotFoundException("해당 회원은 존재하지 않습니다."))
            .when(memberServiceQuerys)
            .getMember(1L);

        assertThatThrownBy(() -> memberService.getAccountInfo(1L))
            .isInstanceOf(MemberNotFoundException.class)
            .hasMessage("해당 회원은 존재하지 않습니다.");
    }



    @Test
    @DisplayName("유효하지 않은 입력에는 예외를 발생시킨다.")
    public void getAccountInfoArgumentException() throws Exception {
        assertThatThrownBy(() -> memberService.getAccountInfo(0L))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("양의 정수를 입력해야 합니다.");
    }

}
