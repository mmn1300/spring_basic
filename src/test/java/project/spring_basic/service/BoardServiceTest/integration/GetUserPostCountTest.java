package project.spring_basic.service.BoardServiceTest.integration;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import project.spring_basic.data.entity.Post;
import project.spring_basic.data.entity.Member;

import project.spring_basic.service.BoardServiceTest.BoardServiceIntegrationTestSupport;

@Tag("integration")
@Tag("service")
@Tag("service-integration")
@Tag("BoardService")
@Tag("BoardService-integration")
public class GetUserPostCountTest extends BoardServiceIntegrationTestSupport {

    // 전체 테스트 실행 전 단 한 번만 실행
	@BeforeAll
    @Override
	public void setUp(){

		// 회원 정보 세팅: 회원1
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



    @Test
    @DisplayName("지정한 사용자가 작성한 게시글의 수를 반환한다. 문자열 아이디 값을 입력받는다.")
    public void getUserPostCount() throws Exception {
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

        Integer countByUser2 = 0;
        Integer countByUser3 = 0;


        // when
        countByUser2 = boardService.getUserPostCount("tttttttt2");
        countByUser3 = boardService.getUserPostCount("tttttttt3");


        // then
        assertThat(countByUser2).isEqualTo(18);
        assertThat(countByUser3).isEqualTo(17);
    }

}