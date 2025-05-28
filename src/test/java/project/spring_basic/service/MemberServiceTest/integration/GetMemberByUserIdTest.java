package project.spring_basic.service.MemberServiceTest.integration;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import project.spring_basic.data.entity.Member;
import project.spring_basic.exception.MemberNotFoundException;
import project.spring_basic.service.MemberServiceTest.MemberServiceIntegrationTestSupport;


@Tag("integration")
@Tag("service")
@Tag("service-integration")
public class GetMemberByUserIdTest extends MemberServiceIntegrationTestSupport {

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
    @DisplayName("문자열 id값을 입력 받아 회원 정보를 반환한다.")
    public void getMemberByUserId() throws Exception {
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
        Member member = memberService.getMemberByUserId("tttttttt");

        // then
        assertThat(member).isNotNull()
                .extracting("id", "userId", "password", "nickname",
                        "email", "phoneNumber", "level"
                    ).contains(
                    1L, "tttttttt", "tttttttt", "테스트용 임시 계정",
                        "ttt@ttt.com", "000-0000-0000", 1
                    );
    }



    @Test
    @DisplayName("존재하지 않는 회원에 대한 메소드 실행에는 예외를 발생시킨다.")
    public void getMemberByUserIdException() throws Exception {
        assertThatThrownBy(() -> memberService.getMemberByUserId("tttttttt"))
            .isInstanceOf(MemberNotFoundException.class)
            .hasMessage("해당 회원은 존재하지 않습니다.");
    }
}
