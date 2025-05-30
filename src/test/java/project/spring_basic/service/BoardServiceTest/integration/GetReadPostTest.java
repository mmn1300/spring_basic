package project.spring_basic.service.BoardServiceTest.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import project.spring_basic.data.dto.Response.ModelAttribute.PostReadDTO;
import project.spring_basic.data.entity.Post;
import project.spring_basic.data.entity.Member;

import project.spring_basic.exception.PostNotFoundException;
import project.spring_basic.service.BoardServiceTest.BoardServiceIntegrationTestSupport;

@Tag("integration")
@Tag("service")
@Tag("service-integration")
@Tag("BoardService")
@Tag("BoardService-integration")
public class GetReadPostTest extends BoardServiceIntegrationTestSupport {

    // 전체 테스트 실행 전 단 한 번만 실행
	@BeforeAll
    @Override
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



    @Test
    @DisplayName("id값에 해당하는 게시글 내용을 반환한다.")
    public void getReadPost() throws Exception {
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

}