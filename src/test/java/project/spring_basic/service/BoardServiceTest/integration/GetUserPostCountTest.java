package project.spring_basic.service.BoardServiceTest.integration;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import project.spring_basic.data.entity.Post;
import project.spring_basic.data.entity.Member;

import project.spring_basic.data.repository.MemberRepository;
// import project.spring_basic.exception.MemberNotFoundException;
import project.spring_basic.service.BoardServiceTest.BoardServiceIntegrationTestSupport;

@Tag("integration")
@Tag("service")
@Tag("service-integration")
@Tag("BoardService")
@Tag("BoardService-integration")
public class GetUserPostCountTest extends BoardServiceIntegrationTestSupport {

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
            
            memberRepository.saveAndFlush(member);
        }


        for (int i=0; i<70; i++){
            Member member = memberRepository.findById(Long.valueOf((i % maxUser) + 1)).get();
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



    // 2025-05-29 리팩터링에 의해 회원 정보 없이 게시글의 등록이 불가해짐으로써
    // 구조적으로 발생 불가능한 시나리오가 되었음.
    //
    // @Test
    // @DisplayName("존재하지 않는 사용자에 대한 메소드 실행에는 예외를 발생시킨다.")
    // public void getUserPostCountMemberException() throws Exception {
    //     assertThatThrownBy(() -> boardService.getUserPostCount("tttttttt"))
    //             .isInstanceOf(MemberNotFoundException.class)
    //             .hasMessage("해당 회원은 존재하지 않습니다.");
    // }

}