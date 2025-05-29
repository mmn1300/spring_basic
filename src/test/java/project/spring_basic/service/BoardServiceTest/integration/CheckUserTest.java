package project.spring_basic.service.BoardServiceTest.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import project.spring_basic.data.entity.Post;
import project.spring_basic.data.entity.Member;

// import project.spring_basic.exception.MemberNotFoundException;
import project.spring_basic.exception.PostNotFoundException;
import project.spring_basic.service.BoardServiceTest.BoardServiceIntegrationTestSupport;

@Tag("integration")
@Tag("service")
@Tag("service-integration")
@Tag("BoardService")
@Tag("BoardService-integration")
public class CheckUserTest extends BoardServiceIntegrationTestSupport {

    // 매 테스트 메서드 종료 시 자동 실행
    @AfterEach
    public void tearDown(){
        // 트랜잭션 생성
        TransactionStatus status = transactionManager.getTransaction(new DefaultTransactionDefinition());

        try {
            // 모든 데이터 삭제
            postRepository.deleteAllInBatch();
            memberRepository.deleteAllInBatch();

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
        memberRepository.saveAndFlush(member);
            
        Post newPost = Post.builder()
                            .member(member)
                            .title("1")
                            .content("1")
                            .createAt(LocalDateTime.now().withNano(0))
                            .build();
        postRepository.save(newPost);

        Boolean result = false;


        // when
        result = boardService.checkUser(1L, "tttttttt");


        // then
        assertThat(result).isTrue();
    }



    @Test
    @DisplayName("존재하지 않는 게시글에 대한 접근에는 예외를 발생시킨다.")
    public void checkUserPostException() throws Exception {
        assertThatThrownBy(() -> boardService.checkUser(1L, "tttttttt"))
                    .isInstanceOf(PostNotFoundException.class)
                    .hasMessage("1번 게시글은 존재하지 않습니다.");;
    }



    // 2025-05-29 리팩터링에 의해 회원 정보 없이 게시글의 등록이 불가해짐으로써
    // 구조적으로 발생 불가능한 시나리오가 되었음.
    //
    // @Test
    // @DisplayName("존재하지 않는 회원에 대한 접근에는 예외를 발생시킨다.")
    // public void checkUserMemberException() throws Exception {
    //     // given
    //     Member member = Member.builder()
    //                         .userId("tttttttt")
    //                         .password("tttttttt")
    //                         .nickname("테스트용 임시 계정")
    //                         .email("ttt@ttt.com")
    //                         .phoneNumber("000-0000-0000")
    //                         .createAt(LocalDateTime.now())
    //                         .level(1)
    //                         .build();

    //     Post newPost = Post.builder()
    //                         .member(member)
    //                         .title("1")
    //                         .content("1")
    //                         .createAt(LocalDateTime.now().withNano(0))
    //                         .build();
    //     postRepository.save(newPost);

    //     // when & then
    //     assertThatThrownBy(() -> boardService.checkUser(1L, "tttttttt"))
    //                 .isInstanceOf(MemberNotFoundException.class)
    //                 .hasMessage("해당 회원은 존재하지 않습니다.");;
    // }

}