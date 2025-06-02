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
public class RemoveTest extends BoardServiceIntegrationTestSupport {

    // 매 테스트 메서드 종료 시 자동 실행
    @AfterEach
    @Override
    public void tearDown() throws Exception{
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
    @DisplayName(
        "데이터베이스에 존재하는 게시글 데이터를 삭제한다." +
        "삭제한 이후 id값을 가진 게시글은 그 값을 하나씩 감소시킨다." +
        "Auto Increment값을 하나 감소시킨다."
    )
    public void remove() throws Exception {
        // given
        Member member = memberRepository.findById(1L).get();

        for (int i=1; i<=5; i++){
            Post newPost = Post.builder()
                .member(member)
                .title(Integer.toString(i))
                .content(Integer.toString(i))
                .createAt(LocalDateTime.now().withNano(0))
                .build();
            postRepository.save(newPost);
        }
        Post newPost = Post.builder()
                .member(member)
                .title("6")
                .content("6")
                .createAt(LocalDateTime.now().withNano(0))
                .build();

        // when
        boardService.remove(3L);

        // then
        Post fourthPost = postRepository.findById(3L).map(p -> p).orElse(null);
        postRepository.save(newPost);
        Post lastPost = postRepository.findLatestPost();

        assertThat(fourthPost).isNotNull()
                    .extracting("id", "title")
                    .contains(3L, "4");

        assertThat(lastPost).isNotNull()
                    .extracting("id", "title")
                    .contains(5L, "6");
    }



    @Test
    @DisplayName("데이터베이스에 존재하는 게시글 중 가장 최근 데이터를 삭제한다.")
    public void removeLatestPost() throws Exception {
        // given
        Member member = memberRepository.findById(1L).get();

        for (int i=1; i<=5; i++){
            Post newPost = Post.builder()
                .member(member)
                .title(Integer.toString(i))
                .content(Integer.toString(i))
                .createAt(LocalDateTime.now().withNano(0))
                .build();
            postRepository.save(newPost);
        }

        // when
        boardService.remove(5L);

        // then
        Post secondToLastPost = postRepository.findById(4L).map(p -> p).orElse(null);
        Post newLastPost = postRepository.findLatestPost();

        assertThat(secondToLastPost).isNotNull()
                .extracting("id", "title")
                .contains(4L, "4");

        assertThat(newLastPost).isNotNull()
                .extracting("id", "title")
                .contains(4L, "4");
    }



    @Test
    @DisplayName("첨부 파일이 존재하는 게시글은 첨부 파일도 함께 삭제한다.")
    public void removePostWithAttachment() throws Exception {
        // given
        String tempName = "test.txt";
        Member member = memberRepository.findById(1L).get();

        for (int i=1; i<=5; i++){
            Post newPost = Post.builder()
                .member(member)
                .title(Integer.toString(i))
                .content(Integer.toString(i))
                .createAt(LocalDateTime.now().withNano(0))
                .build();
            if(i == 3){
                newPost.setTempName(tempName);
            }
            postRepository.save(newPost);
        }

        String uploadDir = UserDefinePath.ABS_PATH + UserDefinePath.FILE_STORAGE_PATH;
        File targetFile = new File(uploadDir, tempName);
        if (!targetFile.exists()) {
            targetFile.createNewFile();
        }

        Path filePath = Paths.get(
                UserDefinePath.ABS_PATH,
                UserDefinePath.FILE_STORAGE_PATH,
                tempName
            );


        // when
        boardService.remove(3L);

        // then
        assertThat(filePath).doesNotExist();
    }

}
