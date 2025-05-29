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

import project.spring_basic.data.dto.Response.ModelAttribute.PostReadDTO;
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
public class GetReadPostTest extends BoardServiceIntegrationTestSupport {

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
    @DisplayName("id값에 해당하는 게시글 내용을 반환한다.")
    public void getReadPost() throws Exception {
        // given
        for (int i=1; i<=2; i++){
            Member member = Member.builder()
                            .userId("tttttttt" + Integer.toString(i))
                            .password("tttttttt")
                            .nickname("테스트용 임시 계정"  + Integer.toString(i))
                            .email("ttt@ttt.com")
                            .phoneNumber("000-0000-0000")
                            .createAt(LocalDateTime.now())
                            .level(1)
                            .build();
            
            memberRepository.saveAndFlush(member);
        }


        for (int i=0; i<3; i++){
            Member member = memberRepository.findById(Long.valueOf((i % 2) + 1)).get();
            Post newPost = Post.builder()
                            .member(member)
                            .title(Integer.toString(i+1))
                            .content(Integer.toString(i+1))
                            .createAt(LocalDateTime.now().withNano(0))
                            .build();

            postRepository.save(newPost);
        }

        PostReadDTO postReadDTO = null;


        // when
        postReadDTO = boardService.getReadPost(3L);


        // then
        assertThat(postReadDTO).isNotNull()
            .extracting("number", "title", "content", "userId", "nickname")
            .contains(3L, "3", "3", "tttttttt1", "테스트용 임시 계정1");
    }



    @Test
    @DisplayName("유효하지 않은 입력에 대한 예외를 발생시킨다.")
    public void getReadPostArgumentException() throws Exception {
        assertThatThrownBy(() -> boardService.getReadPost(0L))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("양의 정수를 입력해야 합니다.");
    }



    @Test
    @DisplayName("존재하지 않는 게시물에 대한 메소드 실행에는 예외를 발생시킨다.")
    public void getReadPostException() throws Exception {
        assertThatThrownBy(() -> boardService.getReadPost(1L))
                    .isInstanceOf(PostNotFoundException.class)
                    .hasMessage("1번 게시글은 존재하지 않습니다.");
    }



    // 2025-05-29 리팩터링에 의해 회원 정보 없이 게시글의 등록이 불가해짐으로써
    // 구조적으로 발생 불가능한 시나리오가 되었음.
    //
    // @Test
    // @DisplayName("존재하지 않는 작성자에 대한 메소드 실행에는 예외를 발생시킨다.")
    // public void getReadPostMemberException() throws Exception {
    //     // given
    //     Member member = Member.builder()
    //                     .userId("tttttttt")
    //                     .password("tttttttt")
    //                     .nickname("테스트용 임시 계정")
    //                     .email("ttt@ttt.com")
    //                     .phoneNumber("000-0000-0000")
    //                     .createAt(LocalDateTime.now())
    //                     .level(1)
    //                     .build();

    //     Post newPost = Post.builder()
    //                     .member(member)
    //                     .title("1")
    //                     .content("1")
    //                     .createAt(LocalDateTime.now().withNano(0))
    //                     .build();

    //     postRepository.save(newPost);


    //     // when & then
    //     assertThatThrownBy(() -> boardService.getReadPost(1L))
    //             .isInstanceOf(MemberNotFoundException.class)
    //             .hasMessage("해당 회원은 존재하지 않습니다.");
    // }
}