package project.spring_basic.repository;

import java.time.LocalDateTime;
import java.util.List;

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
import jakarta.transaction.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

import project.spring_basic.data.entity.Member;
import project.spring_basic.data.entity.Post;
import project.spring_basic.data.repository.MemberRepository;
import project.spring_basic.data.repository.PostRepository;
import project.spring_basic.data.repository.PostRepositoryCustom;


@Tag("unit")
@ActiveProfiles("test")
@SpringBootTest
public class PostRepositoryCustomTest {
    
    @Autowired PostRepository postRepository;

    @Autowired PostRepositoryCustom postRepositoryCustom;

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
    @Transactional
    @DisplayName("Auto Increment 값을 변경한다")
    public void updateAutoIncrement(){
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
        memberRepository.saveAndFlush(member);

        Post newPost = Post.builder()
                .member(member)
                .title("1")
                .content("1")
                .createAt(LocalDateTime.now().withNano(0))
                .build();

        Long newAutoIncrement = 10L;

        
        // when
        postRepositoryCustom.updateAutoIncrement(newAutoIncrement);


        // then
        postRepository.save(newPost);
        List<Post> posts = postRepository.findAll();
        assertThat(posts).hasSize(1);

        assertThat(posts.get(0))
                .extracting("id")
                .isEqualTo(newAutoIncrement);
    }

}
