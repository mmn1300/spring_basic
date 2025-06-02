package project.spring_basic.service.BoardServiceTest.integration;

import static org.assertj.core.api.Assertions.*;

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
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import project.spring_basic.constant.UserDefinePath;
import project.spring_basic.data.entity.Member;
import project.spring_basic.data.entity.Post;

import project.spring_basic.service.BoardServiceTest.BoardServiceIntegrationTestSupport;

@Tag("integration")
@Tag("service")
@Tag("service-integration")
@Tag("BoardService")
@Tag("BoardService-integration")
public class IsFileExistsTest extends BoardServiceIntegrationTestSupport {

    // 매 테스트 메서드 종료 시 자동 실행
    @AfterEach
    @Override
    public void tearDown() throws Exception {
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
    @DisplayName("게시글에 파일이 첨부되었는지에 대한 여부를 반환한다.")
    public void isFileExists() throws Exception {
        // given
        String tempName = "test.txt";
        Member member = memberRepository.findById(1L).get();
        
        Post newPost = Post.builder()
                            .member(member)
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

        // when
        String result = boardService.isFileExists(1L);

        // then
        assertThat(result).isEqualTo(tempName);
    }



    @Test
    @DisplayName("파일이 첨부되지 않은 게시물은 공백 문자열을 반환한다.")
    public void isFileExistsWhenFileDoesNotExist() throws Exception {
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
        String result = boardService.isFileExists(1L);

        // then
        assertThat(result).isEqualTo("");
    }

}