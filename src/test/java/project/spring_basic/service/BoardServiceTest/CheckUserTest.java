package project.spring_basic.service.BoardServiceTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

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
import project.spring_basic.data.entity.Post;
import project.spring_basic.data.entity.Member;

import project.spring_basic.data.repository.MemberRepository;
import project.spring_basic.data.repository.PostRepository;
import project.spring_basic.exception.PostNotFoundException;
import project.spring_basic.service.BoardService;


@Tag("integration")
@ActiveProfiles("test")
@SpringBootTest
public class CheckUserTest {
    
    @Autowired BoardService boardService;

    @Autowired PostRepository postRepository;

    @Autowired MemberRepository memberRepository;

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
            postRepository.deleteAll();
            memberRepository.deleteAll();

            // Auto Increment 값 초기화
            entityManager.createNativeQuery(
                "ALTER TABLE posts ALTER COLUMN id RESTART WITH 1"
            ).executeUpdate();

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
    @DisplayName("게시글에 대한 작성자 일치 여부를 반환한다.")
    public void checkUser() throws Exception {
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
            
        Post newPost = Post.builder()
                            .userId(1L)
                            .title("1")
                            .content("1")
                            .createAt(LocalDateTime.now().withNano(0))
                            .build();

        memberRepository.save(member);
        postRepository.save(newPost);

        Boolean result = false;


        // when
        try {
            result = boardService.checkUser(1L, "tttttttt");
        } catch (Exception e) {
            throw e;
        }


        // then
        assertThat(result).isTrue();
    }



    @Test
    @DisplayName("존재하지 않는 게시글에 대한 접근에는 예외를 발생시킨다.")
    public void checkUserWithNoData() throws Exception {
        assertThatThrownBy(() -> boardService.checkUser(1L, "tttttttt"))
                    .isInstanceOf(PostNotFoundException.class)
                    .hasMessage("1번 게시글은 존재하지 않습니다.");;
    }
}
