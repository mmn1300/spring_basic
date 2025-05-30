package project.spring_basic.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

import project.spring_basic.data.entity.Member;
import project.spring_basic.data.entity.Post;
import project.spring_basic.data.repository.MemberRepository;
import project.spring_basic.data.repository.PostRepository;

@Tag("unit")
@ActiveProfiles("test")
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PostRepositoryTest {
    
    @Autowired PostRepository postRepository;

    @Autowired MemberRepository memberRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private PlatformTransactionManager transactionManager;


	// 전체 테스트 실행 전 단 한 번만 실행
	@BeforeAll
	public void setUp(){

		// 회원 정보 세팅: 회원1, 회원2
		Member member = Member.builder()
				.userId("tttttttt")
				.password("tttttttt")
				.nickname("테스트용 임시 계정")
				.email("ttt@ttt.com")
				.phoneNumber("000-0000-0000")
				.createAt(LocalDateTime.now())
				.level(1)
				.build();

        Member member2 = Member.builder()
				.userId("tttttttt2")
				.password("tttttttt2")
				.nickname("테스트용 임시 계정2")
				.email("ttt2@ttt.com")
				.phoneNumber("000-2222-0000")
				.createAt(LocalDateTime.now())
				.level(1)
				.build();
        memberRepository.saveAllAndFlush(List.of(member, member2));
	}


    // 매 테스트 메서드 종료 시 자동 실행
    @AfterEach
    public void tearDown(){
        // 트랜잭션 생성
        TransactionStatus status = transactionManager.getTransaction(new DefaultTransactionDefinition());

        try {
            // 모든 데이터 삭제
            postRepository.deleteAll();

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
    @Transactional // @Modifying 이 존재하는 함수 실행시 반드시 필요
    @DisplayName("게시글을 삭제하고 이후 아이디를 가진 게시물들의 아이디를 하나씩 감소시킨다.")
    public void updateIdsGreaterThan(){
        // given
		Member member = memberRepository.findById(1L).get();
		Member member2 = memberRepository.findById(2L).get();

        Post post1 = Post.builder()
                .member(member)
                .title("1")
                .content("1")
                .createAt(LocalDateTime.now().withNano(0))
                .build();

        Post post2 = Post.builder()
                .member(member)
                .title("2")
                .content("2")
                .createAt(LocalDateTime.now().withNano(0))
                .build();

        Post post3 = Post.builder()
                .member(member2)
                .title("3")
                .content("3")
                .createAt(LocalDateTime.now().withNano(0))
                .build();
        
        Post post4 = Post.builder()
                .member(member)
                .title("4")
                .content("4")
                .createAt(LocalDateTime.now().withNano(0))
                .build();

        Long deleteContentId = 2L;
        postRepository.saveAll(List.of(post1, post2, post3, post4));
        postRepository.deleteById(deleteContentId);


        // when
        postRepository.updateIdsGreaterThan(deleteContentId);
		entityManager.flush();
		entityManager.clear();

        // then
        List<Post> posts = postRepository.findAll();
        assertThat(posts).hasSize(3)
                .extracting("id")
                .containsExactlyInAnyOrder(
                    1L, 2L, 3L
                )
                .doesNotContain(4L);
    }



    @Test
    @DisplayName("가장 최근에 삽입 된 게시글을 조회한다.")
    public void findLatestPost(){
        // given
        Member member = memberRepository.findById(1L).get();
		Member member2 = memberRepository.findById(2L).get();

        Post post1 = Post.builder()
                .member(member)
                .title("1")
                .content("1")
                .createAt(LocalDateTime.now().withNano(0))
                .build();

        Post post2 = Post.builder()
                .member(member)
                .title("2")
                .content("2")
                .createAt(LocalDateTime.now().withNano(0))
                .build();

        Post post3 = Post.builder()
                .member(member2)
                .title("3")
                .content("3")
                .createAt(LocalDateTime.now().withNano(0))
                .build();


        // when
        postRepository.saveAll(List.of(post1, post2, post3));
        Post post = postRepository.findLatestPost();


        // then
        assertThat(post)
                .extracting("id", "member.id", "title", "content")
                .contains(3L, 2L, "3", "3");
    }



    @Test
    @DisplayName("유저 아이디를 기반으로 페이징 처리를 통해 한 번에 정보를 조회한다.")
    public void findByMemberIdOrderByIdDesc(){
        //given
        Member member = memberRepository.findById(1L).get();
		Member member2 = memberRepository.findById(2L).get();


        LocalDateTime time1 = LocalDateTime.now().withNano(0);
        Post post1 = Post.builder()
                .member(member)
                .title("1")
                .content("1")
                .createAt(time1)
                .build();

        LocalDateTime time2 = LocalDateTime.now().withNano(0);
        Post post2 = Post.builder()
                .member(member)
                .title("2")
                .content("2")
                .createAt(time2)
                .build();

        LocalDateTime time3 = LocalDateTime.now().withNano(0);
        Post post3 = Post.builder()
                .member(member2)
                .title("3")
                .content("3")
                .createAt(time3)
                .build();

        LocalDateTime time4 = LocalDateTime.now().withNano(0);
        Post post4 = Post.builder()
                .member(member)
                .title("4")
                .content("4")
                .createAt(time4)
                .build();


        // 테스트 1 -> 기대값 : 4번, 2번 조회 (순서 중요)
        int pageSize = 2;
        PageRequest pageRequest = PageRequest.of(0, pageSize);
        Long userId = 1L;

        // 테스트 2 -> 기대값 : 1번 조회
        // int pageSize = 2;
        // PageRequest pageRequest = PageRequest.of(1, pageSize);
        // Long userId = 1L;

        // 테스트 3 -> 기대값 : 3번 조회
        // int pageSize = 2;
        // PageRequest pageRequest = PageRequest.of(0, pageSize);
        // Long userId = 2L;

        // 테스트 4 -> 기대값 : 4번, 2번, 1번 조회 (순서 중요)
        // int pageSize = 3;
        // PageRequest pageRequest = PageRequest.of(0, pageSize);
        // Long userId = 1L;

        // 테스트 5 -> 기대값 : 조회 되지 않음
        // int pageSize = 10;
        // PageRequest pageRequest = PageRequest.of(0, pageSize);
        // Long userId = 99L;


        // when
        postRepository.saveAll(List.of(post1, post2, post3, post4));
        Page<Post> posts = postRepository.findByMemberIdOrderByIdDesc(userId, pageRequest);


        // then

        // 테스트 1 검증
        assertThat(posts).hasSize(2)
                .extracting("id", "member.id", "title", "content", "createAt")
                .containsExactly(
                    tuple(4L, userId, "4", "4", time4),
                    tuple(2L, userId, "2", "2", time2)
                );
    
        // 테스트 2 검증
        // assertThat(posts).hasSize(1)
        //         .extracting("id", "userId", "title", "content", "createAt")
        //         .containsExactly(
        //             tuple(1L, userId, "1", "1", time1)
        //         );

        // 테스트 3 검증
        // assertThat(posts).hasSize(1)
        //         .extracting("id", "userId", "title", "content", "createAt")
        //         .containsExactly(
        //             tuple(3L, userId, "3", "3", time3)
        //         );

        // 테스트 4 검증
        // assertThat(posts).hasSize(3)
        //         .extracting("id", "userId", "title", "content", "createAt")
        //         .containsExactly(
        //             tuple(4L, userId, "4", "4", time4),
        //             tuple(2L, userId, "2", "2", time2),
        //             tuple(1L, userId, "1", "1", time1)
        //         );

        // 테스트 5 검증
        // assertThat(posts).hasSize(0);
    }



    @Test
    @DisplayName("유저가 작성한 총 게시글의 개수를 반환한다.")
    public void countByMemberId(){
        //given
        Member member = memberRepository.findById(1L).get();
		Member member2 = memberRepository.findById(2L).get();

        Post post1 = Post.builder()
                .member(member)
                .title("1")
                .content("1")
                .createAt(LocalDateTime.now().withNano(0))
                .build();

        Post post2 = Post.builder()
                .member(member)
                .title("2")
                .content("2")
                .createAt(LocalDateTime.now().withNano(0))
                .build();

        Post post3 = Post.builder()
                .member(member2)
                .title("3")
                .content("3")
                .createAt(LocalDateTime.now().withNano(0))
                .build();
        
        Post post4 = Post.builder()
                .member(member)
                .title("4")
                .content("4")
                .createAt(LocalDateTime.now().withNano(0))
                .build();


        // when
        postRepository.saveAll(List.of(post1, post2, post3, post4));
        Long count = postRepository.countByMemberId(1L);


        // then
        assertThat(count).isEqualTo(3L);
    }
}
