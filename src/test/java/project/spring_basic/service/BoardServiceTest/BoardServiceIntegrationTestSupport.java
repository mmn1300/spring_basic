package project.spring_basic.service.BoardServiceTest;

import java.time.LocalDateTime;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
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
import project.spring_basic.data.repository.PostRepository;
import project.spring_basic.service.BoardService;


@ActiveProfiles("test")
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class BoardServiceIntegrationTestSupport {
    
    @Autowired
    protected BoardService boardService;

    @Autowired
    protected PostRepository postRepository;

    @Autowired
    protected MemberRepository memberRepository;

    @PersistenceContext
    protected EntityManager entityManager;

    @Autowired
    protected PlatformTransactionManager transactionManager;



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



    // 매 테스트 메서드 종료 시 자동 실행
    @AfterEach
    public void tearDown() throws Exception {
        // 트랜잭션 생성
        TransactionStatus status = transactionManager.getTransaction(new DefaultTransactionDefinition());

        try {
            // 모든 데이터 삭제
            postRepository.deleteAllInBatch();

            // Auto Increment 값 초기화
            entityManager.createNativeQuery(
                "ALTER TABLE posts ALTER COLUMN id RESTART WITH 1"
            ).executeUpdate();

            transactionManager.commit(status);
        } catch (Exception e) {
            transactionManager.rollback(status);
            throw e;
        }
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

}
