package project.spring_basic.service.BoardServiceTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import project.spring_basic.constant.UserDefinePath;
import project.spring_basic.data.entity.Post;

import project.spring_basic.data.repository.PostRepository;
import project.spring_basic.exception.PostNotFoundException;
import project.spring_basic.service.BoardService;


@Tag("integration")
@ActiveProfiles("test")
@SpringBootTest
public class BoardServiceGetFileTest {

    @Autowired BoardService boardService;

    @Autowired PostRepository postRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private PlatformTransactionManager transactionManager;


    // 매 테스트 메서드 종료 시 자동 실행
    @AfterEach
    public void tearDown() throws Exception{
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

        // 테스트 파일 존재 시 삭제
        Path filePath = Paths.get(UserDefinePath.ABS_PATH, UserDefinePath.FILE_STORAGE_PATH, "test.txt");
        if (Files.exists(filePath)) {
            try {
                Files.delete(filePath);
            } catch (IOException e) {
                throw e;
            }
        }
    }



    @Test
    @DisplayName("게시글에 첨부된 파일을 반환한다.")
    public void getFile() throws Exception {
        // given
        String tempName = "test.txt";

        Post newPost = Post.builder()
                            .userId(1L)
                            .title("1")
                            .content("1")
                            .createAt(LocalDateTime.now().withNano(0))
                            .build();
        
        newPost.setFileName("test.txt");
        newPost.setFileType("txt");
        newPost.setTempName(tempName);
        postRepository.save(newPost);

        String uploadDir = UserDefinePath.ABS_PATH + UserDefinePath.FILE_STORAGE_PATH;
        File targetFile = new File(uploadDir, tempName);
        if (!targetFile.exists()) {
            targetFile.createNewFile();
        }

        ResponseEntity<?> response = null;

        // when
        response = boardService.getFile(1L);

        // then
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        Resource resource = (Resource) response.getBody();
        assertThat(resource).isNotNull();
        
        if(resource != null)
        assertThat(resource.isReadable()).isTrue();
    }



    @Test
    @DisplayName("게시글에 첨부된 파일이 없을 경우 에러코드를 반환한다.")
    public void getFileWhenFileDoesNotExist() throws Exception {
        // given
        Post newPost = Post.builder()
                            .userId(1L)
                            .title("1")
                            .content("1")
                            .createAt(LocalDateTime.now().withNano(0))
                            .build();

        postRepository.save(newPost);

        ResponseEntity<?> response = null;

        // when
        response = boardService.getFile(1L);

        // then
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isEqualTo("File not found");
    }



    @Test
    @DisplayName("존재하지 않는 게시물에 대한 메소드 실행에는 예외를 발생시킨다.")
    public void getFileException() throws Exception {
        assertThatThrownBy(() -> boardService.getFile(2L))
                    .isInstanceOf(PostNotFoundException.class)
                    .hasMessage("2번 게시글은 존재하지 않습니다.");
    }
}
