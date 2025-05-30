package project.spring_basic.service.BoardServiceTest.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import project.spring_basic.data.dto.Response.ModelAttribute.PostUpdateDTO;
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
public class GetUpdatePostTest extends BoardServiceIntegrationTestSupport {

    // 전체 테스트 실행 전 단 한 번만 실행
	@BeforeAll
	public void setUp(){

		// 회원 정보 세팅: 회원1 ~ 2
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
    }



    // 매 테스트 메서드 종료 시 자동 실행
    @AfterEach
    public void tearDown(){
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



    @Test
    @DisplayName("id값에 해당하는 게시글 내용을 반환한다.")
    public void getUpdatePost() throws Exception {
        // given
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

        PostUpdateDTO postUpdateDTO = null;


        // when
        postUpdateDTO = boardService.getUpdatePost(3L);


        //when
        assertThat(postUpdateDTO).isNotNull();
        assertThat(postUpdateDTO.getTitle()).isEqualTo("3");
        assertThat(postUpdateDTO.getContent()).isEqualTo("3");
        assertThat(postUpdateDTO.getUserId()).isEqualTo("tttttttt1");
        assertThat(postUpdateDTO.getNickname()).isEqualTo("테스트용 임시 계정1");
        assertThat(postUpdateDTO.getFileName()).isNull();
    }



    @Test
    @DisplayName("유효하지 않은 입력에 대한 예외를 발생시킨다.")
    public void getUpdatePostArgumentException() throws Exception {
        assertThatThrownBy(() -> boardService.getReadPost(0L))
                    .isInstanceOf(IllegalArgumentException.class);
    }



    @Test
    @DisplayName("존재하지 않는 게시물에 대한 메소드 실행에는 예외를 발생시킨다.")
    public void getUpdatePostException() throws Exception {
        assertThatThrownBy(() -> boardService.getUpdatePost(1L))
                    .isInstanceOf(PostNotFoundException.class)
                    .hasMessage("1번 게시글은 존재하지 않습니다.");
    }

}