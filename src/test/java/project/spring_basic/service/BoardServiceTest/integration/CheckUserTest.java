package project.spring_basic.service.BoardServiceTest.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

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

    @Test
    @DisplayName("게시글에 대한 작성자 일치 여부를 반환한다.")
    public void checkUser() throws Exception {
        // given
        Member member = memberRepository.findById(1L).get();
            
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

}