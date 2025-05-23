package project.spring_basic.service.MemberServiceTest;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import project.spring_basic.data.entity.Member;
import project.spring_basic.data.repository.MemberRepository;
import project.spring_basic.service.MemberService;


@Tag("integration")
@Tag("service")
@Tag("service-integration")
@ActiveProfiles("test")
@SpringBootTest
public class MemberExistsByIdTest {

    @Autowired
    private MemberService memberService;

    @Autowired
    private MemberRepository memberRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private PlatformTransactionManager transactionManager;


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
    @DisplayName("문자열 id값을 가진 회원이 존재하면 true를 반환한다.")
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
        Boolean result = memberService.memberExistsById("tttttttt");

        // then
        assertThat(result).isTrue();
    }



    @Test
    @DisplayName("문자열 id값을 가진 회원이 존재하지 않으면 false를 반환한다.")
    public void memberExistsByIdWhenFalseCase() throws Exception {
        // when
        Boolean result = memberService.memberExistsById("tttttttt");

        // then
        assertThat(result).isFalse();
    }
}
