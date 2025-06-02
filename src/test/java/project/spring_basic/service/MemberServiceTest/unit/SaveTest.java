package project.spring_basic.service.MemberServiceTest.unit;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import project.spring_basic.exception.DtoNullException;
import project.spring_basic.service.MemberServiceTest.MemberServiceUnitTestSupport;

@Tag("integration")
@Tag("service")
@Tag("service-integration")
@Tag("MemberService")
@Tag("MemberService-integration")
public class SaveTest extends MemberServiceUnitTestSupport {

    @Test
    @DisplayName("DTO가 존재하지 않을 경우에는 예외를 발생시킨다.")
    public void saveDtoNullException() throws Exception {
        assertThatThrownBy(() -> memberService.save(null))
                .isInstanceOf(DtoNullException.class)
                .hasMessage("DTO가 존재하지 않습니다.");
    }

}
