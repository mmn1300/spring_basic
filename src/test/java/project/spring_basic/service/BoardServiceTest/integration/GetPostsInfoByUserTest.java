package project.spring_basic.service.BoardServiceTest.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import project.spring_basic.data.PostInfo;
import project.spring_basic.data.dto.Response.Json.PostsDTO;
import project.spring_basic.data.entity.Post;
import project.spring_basic.data.entity.Member;

// import project.spring_basic.exception.MemberNotFoundException;
import project.spring_basic.service.BoardServiceTest.BoardServiceIntegrationTestSupport;

@Tag("integration")
@Tag("service")
@Tag("service-integration")
@Tag("BoardService")
@Tag("BoardService-integration")
public class GetPostsInfoByUserTest extends BoardServiceIntegrationTestSupport {

    // 전체 테스트 실행 전 단 한 번만 실행
	@BeforeAll
	public void setUp(){

		// 회원 정보 세팅: 회원1 ~ 4
		for (int i=1; i<=4; i++){
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
    @DisplayName("지정한 사용자가 작성한 게시글에서 페이지 번호에 해당하는 만큼만 반환한다.")
    public void getPostsInfoByUser() throws Exception {
        // given
        for (int i=0; i<70; i++){
            Member member = memberRepository.findById(Long.valueOf((i % 4) + 1)).get();
            Post newPost = Post.builder()
                            .member(member)
                            .title(Integer.toString(i))
                            .content(Integer.toString(i))
                            .createAt(LocalDateTime.now().withNano(0))
                            .build();

            postRepository.save(newPost);
        }

        PostsDTO postsDTO = null;

        // when
        postsDTO = boardService.getPostsInfoByUser(1, 2L);


        // then
        assertThat(postsDTO).isNotNull();
        assertThat(postsDTO.getRows()).isEqualTo(16);

        List<PostInfo> posts = postsDTO.getPosts();
        PostInfo firstUser = posts.get(0);
        PostInfo lastUser = posts.get(posts.size() - 1);

        assertThat(firstUser.getId()).isEqualTo(70);
        assertThat(firstUser.getUserId()).isEqualTo("tttttttt2");

        assertThat(lastUser.getId()).isEqualTo(10);
        assertThat(lastUser.getUserId()).isEqualTo("tttttttt2");
    }



    @Test
    @DisplayName("아무런 데이터도 존재하지 않을 경우 row값은 0을, 리스트는 empty로 반환한다")
    public void getPostsInfoWithNoData() throws Exception {
        // given
        PostsDTO postsDTO = null;


        // when
        postsDTO = boardService.getPostsInfoByUser(1, 1L);


        // then
        assertThat(postsDTO).isNotNull();
        assertThat(postsDTO.getRows()).isZero();
        assertThat(postsDTO.getPosts()).isEmpty();
    }



    @Test
    @DisplayName("유효하지 않은 입력에 대한 예외를 발생시킨다.")
    public void getPostsInfoByUserException() throws Exception {
        assertThatThrownBy(() -> boardService.getPostsInfoByUser(0, 1L))
                    .isInstanceOf(IllegalArgumentException.class);

        assertThatThrownBy(() -> boardService.getPostsInfoByUser(1, 0L))
                    .isInstanceOf(IllegalArgumentException.class);
    }

}
