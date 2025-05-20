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
import project.spring_basic.exception.MemberNotFoundException;
import project.spring_basic.service.BoardService;


@Tag("integration")
@ActiveProfiles("test")
@SpringBootTest
public class GetUserPostCountTest {
    
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
    @DisplayName("지정한 사용자가 작성한 게시글의 수를 반환한다. 문자열 아이디 값을 입력받는다.")
    public void getUserPostCount() throws Exception {
        // given
        int maxUser = 4;

        for (int i=1; i<=maxUser; i++){
            Member member = Member.builder()
                            .userId("tttttttt" + Integer.toString(i))
                            .password("tttttttt")
                            .nickname("테스트용 임시 계정"  + Integer.toString(i))
                            .email("ttt@ttt.com")
                            .phoneNumber("000-0000-0000")
                            .createAt(LocalDateTime.now())
                            .level(1)
                            .build();
            
            memberRepository.save(member);
        }


        for (int i=0; i<70; i++){
            Post newPost = Post.builder()
                            .userId(Long.valueOf((i % maxUser) + 1))
                            .title(Integer.toString(i))
                            .content(Integer.toString(i))
                            .createAt(LocalDateTime.now().withNano(0))
                            .build();

            postRepository.save(newPost);
        }

        Integer countByUser2 = 0;
        Integer countByUser3 = 0;

        // when
        try{
            countByUser2 = boardService.getUserPostCount("tttttttt2");
            countByUser3 = boardService.getUserPostCount("tttttttt3");
        }catch(Exception e){
            throw e;
        }

        //when
        assertThat(countByUser2).isEqualTo(18);
        assertThat(countByUser3).isEqualTo(17);
    }



    @Test
    @DisplayName("존재하지 않는 사용자에 대한 메소드 실행에는 예외를 발생시킨다.")
    public void getUserPostCountMemberException() throws Exception {
        assertThatThrownBy(() -> boardService.getUserPostCount("tttttttt"))
                .isInstanceOf(MemberNotFoundException.class)
                .hasMessage("해당 회원은 존재하지 않습니다.");
    }
}
