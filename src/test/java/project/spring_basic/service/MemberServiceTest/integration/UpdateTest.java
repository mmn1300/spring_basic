package project.spring_basic.service.MemberServiceTest.integration;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import project.spring_basic.data.dto.Request.NewAccountDTO;
import project.spring_basic.data.entity.Member;
import project.spring_basic.exception.DtoNullException;
import project.spring_basic.exception.MemberNotFoundException;
import project.spring_basic.service.MemberServiceTest.MemberServiceIntegrationTestSupport;


@Tag("integration")
@Tag("service")
@Tag("service-integration")
public class UpdateTest extends MemberServiceIntegrationTestSupport {

    // 매 테스트 메서드 종료 시 자동 실행
    @AfterEach
    public void tearDown(){
        // 트랜잭션 생성
        TransactionStatus status = transactionManager.getTransaction(new DefaultTransactionDefinition());

        try {
            // 모든 데이터 삭제
            memberRepository.deleteAllInBatch();

            // Auto Increment 값 초기화
            entityManager.createNativeQuery(
                "ALTER TABLE members ALTER COLUMN id RESTART WITH 1"
            ).executeUpdate();

            transactionManager.commit(status);
        } catch (Exception e) {
            transactionManager.rollback(status);
            throw e;
        }
    }


    @Test
    @DisplayName("수정된 회원 정보를 데이터베이스에 갱신한다.")
    public void update() throws Exception {
        // given
        Member member = Member.builder()
            .userId("tttttttt")
            .password("tttttttt")
            .nickname("테스트용 임시 계정")
            .email("ttt@ttt.com")
            .phoneNumber("000-0000-0000")
            .createAt(LocalDateTime.now())
            .level(1)
            .build();
        
        memberRepository.save(member);

        NewAccountDTO newAccountDTO = new NewAccountDTO(
                "newtttttttt",
                "새 테스트용 임시 계정",
                "newttt@ttt.com",
                "000-0000-0000"
            );


        // when
        memberService.update(newAccountDTO, 1L);

        // then
        Member UpdatedMember = memberRepository.findById(1L).map(m ->m).orElse(null);

        assertThat(UpdatedMember).isNotNull()
                .extracting(
                    "id", "userId", "password", "nickname",
                    "email", "phoneNumber", "level"
                ).contains(
                    1L, "newtttttttt", "tttttttt", "새 테스트용 임시 계정", 
                    "newttt@ttt.com", "000-0000-0000", 1
                );
    }



    @Test
    @DisplayName("DTO가 존재하지 않을 경우에는 예외를 발생시킨다.")
    public void updateDtoNullException() throws Exception {
        // given
        Member member = Member.builder()
            .userId("tttttttt")
            .password("tttttttt")
            .nickname("테스트용 임시 계정")
            .email("ttt@ttt.com")
            .phoneNumber("000-0000-0000")
            .createAt(LocalDateTime.now())
            .level(1)
            .build();
        
        memberRepository.save(member);

        // when & then
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
