package project.spring_basic.service.BoardServiceTest.querys.integretion;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import project.spring_basic.data.entity.Member;
import project.spring_basic.data.entity.Post;
import project.spring_basic.exception.PostNotFoundException;
import project.spring_basic.service.BoardServiceTest.BoardServiceIntegrationTestSupport;
import project.spring_basic.service.querys.BoardServiceQuerys;

@Tag("integration")
@Tag("service")
@Tag("service-integration")
@Tag("BoardService")
@Tag("BoardService-integration")
public class GetPostTest extends BoardServiceIntegrationTestSupport {
    
    @Autowired BoardServiceQuerys boardServiceQuerys;


    @Test
    @DisplayName("게시글 번호에 해당하는 게시글을 읽어와 반환한다.")
    public void getPost() throws Exception {
        // given
        Member member = memberRepository.findById(1L).get();
            
        Post newPost = Post.builder()
                .member(member)
                .title("1")
                .content("1")
                .createAt(LocalDateTime.now().withNano(0))
                .build();
        postRepository.save(newPost);


        // when
        Post actualPost = boardServiceQuerys.getPost(1L);


        // then
        assertThat(actualPost).isNotNull()
            .extracting("id", "member.id", "title", "content")
            .containsExactly(
                1L, 1L, "1", "1"
            );
    }



    @Test
    @DisplayName("존재하지 않는 게시물에 대한 메소드 실행에는 예외를 발생시킨다.")
    public void getPostException() throws Exception {
        assertThatThrownBy(() -> boardServiceQuerys.getPost(1L))
                        .isInstanceOf(PostNotFoundException.class)
                        .hasMessage("1번 게시글은 존재하지 않습니다.");
    }
}
