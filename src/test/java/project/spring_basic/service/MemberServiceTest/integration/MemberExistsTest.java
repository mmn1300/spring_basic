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
import project.spring_basic.service.MemberServiceTest.MemberServiceIntegrationTestSupport;


@Tag("integration")
@Tag("service")
@Tag("service-integration")
public class MemberExistsTest extends MemberServiceIntegrationTestSupport {


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



    // 문자열 id 일치 - 비밀번호 일치
    @Test
    @DisplayName("알맞는 문자열 id값과 비밀번호을 가진 회원이 존재하면 true를 반환한다.")
    public void memberExistsById() throws Exception {
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

        // when
        Boolean result = memberService.memberExists("tttttttt", "tttttttt");

        // then
        assertThat(result).isTrue();
    }



    // 문자열 id 불 일치 - 비밀번호 일치
    @Test
    @DisplayName("문자열 id값이 다르면 false를 반환한다.")
    public void memberExistsById2() throws Exception {
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

        // when
        Boolean result = memberService.memberExists("aaaaaaaa", "tttttttt");

        // then
        assertThat(result).isFalse();
    }



    // 문자열 id 일치 - 비밀번호 불 일치
    @Test
    @DisplayName("비밀번호가 다르면 false를 반환한다.")
    public void memberExistsById3() throws Exception {
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

        // when
        Boolean result = memberService.memberExists("tttttttt", "aaaaaaaa");

        // then
        assertThat(result).isFalse();
    }



    // 문자열 id 불 일치 - 비밀번호 불 일치
    @Test
    @DisplayName("문자열 id값과 비밀번호가 둘 다 다르면 false를 반환한다.")
    public void memberExistsById4() throws Exception {
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

        // when
        Boolean result = memberService.memberExists("aaaaaaaa", "aaaaaaaa");

        // then
        assertThat(result).isFalse();
    }
}
