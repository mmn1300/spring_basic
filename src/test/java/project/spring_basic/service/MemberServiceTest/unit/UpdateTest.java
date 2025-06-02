package project.spring_basic.service.MemberServiceTest.unit;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import project.spring_basic.data.dto.Request.NewAccountDTO;
import project.spring_basic.exception.DtoNullException;
import project.spring_basic.exception.MemberNotFoundException;
import project.spring_basic.service.commands.MemberServiceCommands;
import project.spring_basic.service.imp.MemberServiceImp;
import project.spring_basic.service.querys.MemberServiceQuerys;

@Tag("integration")
@Tag("service")
@Tag("service-integration")
@Tag("MemberService")
@Tag("MemberService-integration")
@ExtendWith(MockitoExtension.class)
public class UpdateTest {
    
    @Mock
    private MemberServiceCommands memberServiceCommands;

    @Mock
    private MemberServiceQuerys memberServiceQuerys;

    @InjectMocks
    private MemberServiceImp memberService;


    @Test
    @DisplayName("DTO가 존재하지 않을 경우에는 예외를 발생시킨다.")
    public void updateDtoNullException() throws Exception {
        assertThatThrownBy(() -> memberService.update(null, 1L))
                .isInstanceOf(DtoNullException.class)
                .hasMessage("DTO가 존재하지 않습니다.");
    }



    @Test
    @DisplayName("존재하지 않는 회원에 대한 메소드 실행에는 예외를 발생시킨다.")
    public void updateMemberException() throws Exception {
        // given
        NewAccountDTO newAccountDTO = new NewAccountDTO(
                "newtttttttt",
                "새 테스트용 임시 계정",
                "newttt@ttt.com",
                "000-0000-0000"
            );

        Mockito.doThrow(new MemberNotFoundException("해당 회원은 존재하지 않습니다."))
            .when(memberServiceQuerys)
            .getMember(1L);

        
        // when & then
        assertThatThrownBy(() -> memberService.update(newAccountDTO, 1L))
            .isInstanceOf(MemberNotFoundException.class)
            .hasMessage("해당 회원은 존재하지 않습니다.");
    }



    @Test
    @DisplayName("유효하지 않은 입력에는 예외를 발생시킨다.")
    public void updateArgumentException() throws Exception {
        // given
        NewAccountDTO newAccountDTO = new NewAccountDTO(
                "newtttttttt",
                "새 테스트용 임시 계정",
                "newttt@ttt.com",
                "000-0000-0000"
            );
        
        // when & then
        assertThatThrownBy(() -> memberService.update(newAccountDTO, 0L))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("양의 정수를 입력해야 합니다.");
    }
    
}
