package project.spring_basic.service.MemberServiceTest.integration;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import project.spring_basic.data.dto.Response.ModelAttribute.AccountInfoDTO;
import project.spring_basic.data.entity.Member;
import project.spring_basic.exception.MemberNotFoundException;


@Tag("integration")
@Tag("service")
@Tag("service-integration")
public class GetAccountInfoTest extends MemberServiceIntegrationTestSupport {

    // 매 테스트 메서드 종료 시 자동 실행
    @AfterEach
    public void tearDown(){
        // 트랜잭션 생성
        TransactionStatus status = transactionManager.getTransaction(new DefaultTransactionDefinition());

        try {
            // 모든 데이터 삭제
            memberRepository.deleteAll();

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
    @DisplayName("id 값을 가진 회원의 정보를 AccountInfoDTO 인스턴스로 반환한다.")
    public void getAccountInfo() throws Exception {
        // given
        Member newMember = Member.builder()
            .userId("tttttttt")
            .password("tttttttt")
            .nickname("테스트용 임시 계정")
            .email("ttt@ttt.com")
            .phoneNumber("000-0000-0000")
            .createAt(LocalDateTime.now())
            .level(1)
            .build();

        memberRepository.save(newMember);

        // when
        AccountInfoDTO accountInfoDTO = memberService.getAccountInfo(1L);

        // then
        assertThat(accountInfoDTO).isNotNull()
                .extracting("id", "userId", "nickname", "email", "phone")
                .contains(1L, "tttttttt", "테스트용 임시 계정", "ttt@ttt.com", "000-0000-0000");
    }



    @Test
    @DisplayName("존재하지 않는 회원에 대한 메소드 실행에는 예외를 발생시킨다.")
    public void getAccountInfoMemberException() throws Exception {
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
