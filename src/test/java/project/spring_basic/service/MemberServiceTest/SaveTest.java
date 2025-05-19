package project.spring_basic.service.MemberServiceTest;

import static org.assertj.core.api.Assertions.*;

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
import project.spring_basic.data.dto.Request.MemberDTO;
import project.spring_basic.data.entity.Member;
import project.spring_basic.data.repository.MemberRepository;
import project.spring_basic.exception.DtoNullException;
import project.spring_basic.service.MemberService;


@Tag("integration")
@ActiveProfiles("test")
@SpringBootTest
public class SaveTest {
        
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
    @DisplayName("회원 정보를 데이터베이스에 저장한다.")
    public void save() throws Exception {
        // given
        MemberDTO memberDTO = new MemberDTO(
                "tttttttt",
                "tttttttt",
                "테스트용 임시 계정",
                "ttt@ttt.com",
                "000-0000-0000"
            );

        // when
        memberService.save(memberDTO);

        // then
        Member member = memberRepository.findById(1L).map(m ->m).orElse(null);

        assertThat(member).isNotNull()
                .extracting(
                    "id", "userId", "password", "nickname",
                    "email", "phoneNumber", "level"
                ).contains(
                    1L, "tttttttt", "tttttttt", "테스트용 임시 계정", 
                    "ttt@ttt.com", "000-0000-0000", 1
                );
    }


    @Test
    @DisplayName("DTO가 존재하지 않을 경우에는 예외를 발생시킨다.")
    public void saveDtoNullException() throws Exception {
        assertThatThrownBy(() -> memberService.save(null))
                .isInstanceOf(DtoNullException.class)
                .hasMessage("DTO가 존재하지 않습니다.");
    }
}
