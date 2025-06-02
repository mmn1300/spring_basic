package project.spring_basic.service.MemberServiceTest.unit;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import project.spring_basic.exception.DtoNullException;
import project.spring_basic.service.commands.MemberServiceCommands;
import project.spring_basic.service.imp.MemberServiceImp;
import project.spring_basic.service.querys.MemberServiceQuerys;

@Tag("integration")
@Tag("service")
@Tag("service-integration")
@Tag("MemberService")
@Tag("MemberService-integration")
@ExtendWith(MockitoExtension.class)
public class SaveTest {
    
    @Mock
    private MemberServiceCommands memberServiceCommands;

    @Mock
    private MemberServiceQuerys memberServiceQuerys;

    @InjectMocks
    private MemberServiceImp memberService;



    @Test
    @DisplayName("DTO가 존재하지 않을 경우에는 예외를 발생시킨다.")
    public void saveDtoNullException() throws Exception {
        assertThatThrownBy(() -> memberService.save(null))
                .isInstanceOf(DtoNullException.class)
                .hasMessage("DTO가 존재하지 않습니다.");
    }

}
