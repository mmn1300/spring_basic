package project.spring_basic.service.BoardServiceTest.integration;

import static org.assertj.core.api.Assertions.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.web.multipart.MultipartFile;

import project.spring_basic.constant.UserDefinePath;
import project.spring_basic.data.dto.Request.PostDTO;
import project.spring_basic.data.entity.Member;
import project.spring_basic.data.entity.Post;

import project.spring_basic.exception.DtoNullException;
import project.spring_basic.service.BoardServiceTest.BoardServiceIntegrationTestSupport;

@Tag("integration")
@Tag("service")
@Tag("service-integration")
@Tag("BoardService")
@Tag("BoardService-integration")
public class SaveTest extends BoardServiceIntegrationTestSupport {

    // 전체 테스트 실행 전 단 한 번만 실행
	@BeforeAll
	public void setUp(){

		// 회원 정보 세팅: 회원1
		Member member = Member.builder()
                .userId("tttttttt")
                .password("tttttttt")
                .nickname("테스트용 임시 계정")
                .email("ttt@ttt.com")
                .phoneNumber("000-0000-0000")
                .createAt(LocalDateTime.now())
                .level(1)
                .build();
        memberRepository.saveAndFlush(member);
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
    @DisplayName("게시글을 데이터베이스에 저장한다. 첨부 파일은 존재하지 않는다")
    public void save() throws Exception {
        // given
        PostDTO postDTO = new PostDTO("1", "1");
        Long userId = 1L;
        MultipartFile file = null;

        // when
        boardService.save(postDTO, userId, file);

        // then
        List<Post> posts = postRepository.findAll();
        assertThat(posts).hasSize(1);
        assertThat(posts.get(0)).extracting(
            "id", "member.id", "title", "content",
            "fileName", "fileType", "tempName"
            )
            .contains(1L, 1L, "1", "1", null, null, null);
    }



    @Test
    @DisplayName("게시글을 데이터베이스에 저장한다. 첨부 파일이 존재한다")
    public void saveWithAttachment() throws Exception {
        // given
        PostDTO postDTO = new PostDTO("1", "1");
        Long userId = 1L;

        MultipartFile file = new MockMultipartFile(
                    "file",
                    "test.txt",
                    "text/plain",
                    "테스트 파일".getBytes(StandardCharsets.UTF_8)
        );

        // when
        boardService.save(postDTO, userId, file);

        // then
        List<Post> posts = postRepository.findAll();
        assertThat(posts).hasSize(1);
        assertThat(posts.get(0)).extracting(
            "id", "member.id", "title", "content",
            "fileName", "fileType"
            )
            .contains(1L, 1L, "1", "1", "test.txt", "txt");
        assertThat(posts.get(0).getTempName()).isNotNull()
                                    .isNotEqualTo("test.txt")
                                    .endsWith(".txt");

        Path filePath = Paths.get(
                        UserDefinePath.ABS_PATH,
                        UserDefinePath.FILE_STORAGE_PATH,
                        posts.get(0).getTempName()
                    );

        assertThat(filePath).exists();

        // 파일 존재 시 삭제
        if (Files.exists(filePath)) {
            try {
                Files.delete(filePath);
            } catch (IOException e) {
                throw e;
            }
        }
    }



    @Test
    @DisplayName("DTO가 존재하지 않을 경우에는 예외를 발생시킨다.")
    public void saveDtoNullException() throws Exception {
        assertThatThrownBy(() -> boardService.save(null, 1L, null))
                .isInstanceOf(DtoNullException.class)
                .hasMessage("DTO가 존재하지 않습니다.");
    }


    @Test
    @DisplayName("유효하지 않은 입력에는 예외를 발생시킨다.")
    public void saveArgumentException() throws Exception {
        // given
        PostDTO postDTO = new PostDTO("1", "1");

        // when & then
        assertThatThrownBy(() -> boardService.save(postDTO, 0L, null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("양의 정수를 입력해야 합니다.");
    }
}
