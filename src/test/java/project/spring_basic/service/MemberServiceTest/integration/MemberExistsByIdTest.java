package project.spring_basic.service.MemberServiceTest.integration;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
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
public class MemberExistsByIdTest extends MemberServiceIntegrationTestSupport {

    // 전체 테스트 실행 전 단 한 번만 실행
	@BeforeAll
	public void setUp(){

		// 회원 정보 세팅: 회원1
		Member member = Member.builder()
                .userId("tttttttt")
                .password("tttttttt")
                .nickname("테스트용 임시 계정")
                .email("ttt@ttt.com")
                .phoneNumber("000-0000-0000")
                .createAt(LocalDateTime.now())
                .level(1)
                .build();
        memberRepository.saveAndFlush(member);
	}



    // 전체 테스트 실행 후 단 한 번만 실행
	@AfterAll
	public void cleanUp(){
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
    @DisplayName("문자열 id값을 가진 회원이 존재하면 true를 반환한다.")
    public void memberExistsById() throws Exception {
        // given
        // setUp()에 존재함.

        // when
        Boolean result = memberService.memberExistsById("tttttttt");

        // then
        assertThat(result).isTrue();
    }



    @Test
    @DisplayName("문자열 id값을 가진 회원이 존재하지 않으면 false를 반환한다.")
    public void memberExistsByIdWhenFalseCase() throws Exception {
        // when
        Boolean result = memberService.memberExistsById("aaaaaaaa");

        // then
        assertThat(result).isFalse();
    }
}
