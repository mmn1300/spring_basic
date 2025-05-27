package project.spring_basic.service.BoardServiceTest.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import project.spring_basic.data.PostInfo;
import project.spring_basic.data.dto.Response.Json.PostsDTO;
import project.spring_basic.data.entity.Post;
import project.spring_basic.data.entity.Member;

import project.spring_basic.data.repository.MemberRepository;
import project.spring_basic.exception.MemberNotFoundException;

@Tag("integration")
@Tag("service")
@Tag("service-integration")
@Tag("BoardService")
@Tag("BoardService-integration")
public class GetPostsInfoTest extends BoardServiceIntegrationTestSupport {

    @Autowired MemberRepository memberRepository;


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
    @DisplayName("페이지 번호에 해당하는 게시글들을 반환한다")
    public void getPostsInfo() throws Exception {
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


        for (int i=1; i<=20; i++){
            Post newPost = Post.builder()
                            .userId(1L)
                            .title(Integer.toString(i))
                            .content(Integer.toString(i))
                            .createAt(LocalDateTime.now().withNano(0))
                            .build();

            postRepository.save(newPost);
        }

        PostsDTO postsDTO = null;

        // when
        try{
            postsDTO = boardService.getPostsInfo(1);
        }catch(Exception e){
            throw e;
        }

        //when
        assertThat(postsDTO).isNotNull();
        assertThat(postsDTO.getRows()).isEqualTo(16);

        List<PostInfo> posts = postsDTO.getPosts();
        assertThat(posts.get(0).getId()).isEqualTo(20);
        assertThat(posts.get(posts.size() - 1).getId()).isEqualTo(5);
    }



    @Test
    @DisplayName("아무런 데이터도 존재하지 않을 경우 row값은 0을, 리스트는 empty로 반환한다")
    public void getPostsInfoWithNoData() throws Exception {
        // given
        PostsDTO postsDTO = null;

        // when
        try{
            postsDTO = boardService.getPostsInfo(1);
        }catch(Exception e){
            throw e;
        }

        //when
        assertThat(postsDTO).isNotNull();
        assertThat(postsDTO.getRows()).isZero();
        assertThat(postsDTO.getPosts()).isEmpty();
    }



    @Test
    @DisplayName("페이지 번호에 해당하는 게시글들을 반환한다. 게시글들은 여러 사용자가 작성하였다.")
    public void getPostsInfoInMultiUser() throws Exception {
        // given
        int maxUser = 8;

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


        for (int i=0; i<20; i++){
            Post newPost = Post.builder()
                            .userId(Long.valueOf((i % maxUser) + 1))
                            .title(Integer.toString(i))
                            .content(Integer.toString(i))
                            .createAt(LocalDateTime.now().withNano(0))
                            .build();

            postRepository.save(newPost);
        }

        PostsDTO postsDTO = null;

        // when
        try{
            postsDTO = boardService.getPostsInfo(1);
        }catch(Exception e){
            throw e;
        }

        //when
        assertThat(postsDTO).isNotNull();
        assertThat(postsDTO.getRows()).isEqualTo(16);

        List<PostInfo> posts = postsDTO.getPosts();
        PostInfo firstUser = posts.get(0);
        PostInfo lastUser = posts.get(posts.size() - 1);

        assertThat(firstUser.getId()).isEqualTo(20);
        assertThat(firstUser.getUserId()).isEqualTo("tttttttt" + Integer.toString((20 % maxUser)));

        assertThat(lastUser.getId()).isEqualTo(5);
        assertThat(lastUser.getUserId()).isEqualTo("tttttttt" + Integer.toString((5 % maxUser)));
    }



    @Test
    @DisplayName("유효하지 않은 입력에 대한 예외를 발생시킨다.")
    public void getPostsInfoException() throws Exception {
        assertThatThrownBy(() -> boardService.getPostsInfo(0))
                    .isInstanceOf(IllegalArgumentException.class);
    }



    @Test
    @DisplayName("존재하지 않는 작성자에 대한 메소드 실행에는 예외를 발생시킨다.")
    public void getPostsInfoMemberException() throws Exception {
        // given
        for (int i=1; i<=20; i++){
            Post newPost = Post.builder()
                            .userId(1L)
                            .title(Integer.toString(i))
                            .content(Integer.toString(i))
                            .createAt(LocalDateTime.now().withNano(0))
                            .build();

            postRepository.save(newPost);
        }

        // when & then
        assertThatThrownBy(() -> boardService.getPostsInfo(1))
                .isInstanceOf(MemberNotFoundException.class)
                .hasMessage("해당 회원은 존재하지 않습니다.");
    }
}
