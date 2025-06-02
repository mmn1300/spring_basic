package project.spring_basic.service.BoardServiceTest.integration;

import static org.assertj.core.api.Assertions.*;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import project.spring_basic.constant.UserDefinePath;
import project.spring_basic.data.dto.Request.PostDTO;
import project.spring_basic.data.entity.Member;
import project.spring_basic.data.entity.Post;

import project.spring_basic.service.BoardServiceTest.BoardServiceIntegrationTestSupport;

@Tag("integration")
@Tag("service")
@Tag("service-integration")
@Tag("BoardService")
@Tag("BoardService-integration")
public class UpdateTest extends BoardServiceIntegrationTestSupport {

    // 기존 파일 미 존재 -> 갱신 요청 파일 미 존재
    @Test
    @DisplayName("수정된 게시글을 데이터베이스에 갱신한다. 첨부 파일은 존재하지 않는다")
    public void update() throws Exception {
        // given
        Long userId = 1L;
        Member member = memberRepository.findById(1L).get();

        Post newPost = Post.builder()
                .member(member)
                .title("0")
                .content("0")
                .createAt(LocalDateTime.now().withNano(0))
                .build();
        postRepository.save(newPost);

        Thread.sleep(50);

        PostDTO postDTO = new PostDTO("1", "1");
        MultipartFile file = null;

        // when
        boardService.update(userId, postDTO, file);

        // then
        List<Post> posts = postRepository.findAll();
        assertThat(posts).hasSize(1);

        Post post = posts.get(0);
        assertThat(post).extracting(
                    "id", "member.id", "title", "content",
                    "fileName", "fileType", "tempName"
                    )
                .contains(1L, 1L, "1", "1", null, null, null);
        
        assertThat(post.getUpdateAt()).isAfter(post.getCreateAt());
    }



    // 기존 파일 미 존재 -> 갱신 요청 파일 존재
    @Test
    @DisplayName("수정된 게시글을 데이터베이스에 갱신한다. 수정된 게시글은 첨부 파일이 존재한다")
    public void updateWithAttachment() throws Exception {
        // given
        Long userId = 1L;
        Member member = memberRepository.findById(1L).get();

        Post newPost = Post.builder()
                .member(member)
                .title("0")
                .content("0")
                .createAt(LocalDateTime.now().withNano(0))
                .build();
        postRepository.save(newPost);

        PostDTO postDTO = new PostDTO("1", "1");
        MultipartFile file = new MockMultipartFile(
                    "file",
                    "test.txt",
                    "text/plain",
                    "테스트 파일".getBytes(StandardCharsets.UTF_8)
        );

        // when
        boardService.update(userId, postDTO, file);

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



    // 기존 파일 존재 -> 갱신 요청 파일 미 존재
    @Test
    @DisplayName("수정된 게시글을 데이터베이스에 갱신한다. 기존 파일은 유지한다.")
    public void update2() throws Exception {
        // given
        String tempName = "test.txt";
        Long userId = 1L;
        Member member = memberRepository.findById(1L).get();

        Post newPost = Post.builder()
                .member(member)
                .title("0")
                .content("0")
                .createAt(LocalDateTime.now().withNano(0))
                .build();
        newPost.setFileName(tempName);
        newPost.setFileType("txt");
        newPost.setTempName(tempName);
        
        postRepository.save(newPost);

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

        PostDTO postDTO = new PostDTO("1", "1");
        MultipartFile file = null;

        // when
        boardService.update(userId, postDTO, file);

        // then
        List<Post> posts = postRepository.findAll();
        assertThat(posts).hasSize(1);
        assertThat(posts.get(0)).extracting(
                    "id", "member.id", "title", "content",
                    "fileName", "fileType", "tempName"
                    )
                .contains(1L, 1L, "1", "1", "test.txt", "txt", "test.txt");

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



    // 기존 파일 존재 -> 갱신 요청 파일 존재
    @Test
    @DisplayName("수정된 게시글을 데이터베이스에 갱신한다. 기존 파일 또한 교체 갱신한다.")
    public void updateWithAttachment2() throws Exception {
        // given
        String tempName = "test.txt";
        String newFileName = "new_test.txt";
        Long userId = 1L;
        Member member = memberRepository.findById(1L).get();

        Post newPost = Post.builder()
                .member(member)
                .title("0")
                .content("0")
                .createAt(LocalDateTime.now().withNano(0))
                .build();
        newPost.setFileName(tempName);
        newPost.setFileType("txt");
        newPost.setTempName(tempName);
        
        postRepository.save(newPost);

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


        PostDTO postDTO = new PostDTO("1", "1");
        MultipartFile file = new MockMultipartFile(
                    "file",
                    newFileName,
                    "text/plain",
                    "새 테스트 파일".getBytes(StandardCharsets.UTF_8)
        );


        // when
        boardService.update(userId, postDTO, file);


        // then
        List<Post> posts = postRepository.findAll();
        assertThat(posts).hasSize(1);

        assertThat(posts.get(0)).extracting(
                    "id", "member.id", "title", "content",
                    "fileName", "fileType"
                    )
                .contains(1L, 1L, "1", "1", newFileName, "txt");
        
        assertThat(posts.get(0).getTempName()).isNotNull()
                                    .isNotEqualTo(newFileName)
                                    .endsWith(".txt");

        assertThat(filePath).doesNotExist();

        Path newFilePath = Paths.get(
                UserDefinePath.ABS_PATH,
                UserDefinePath.FILE_STORAGE_PATH,
                posts.get(0).getTempName()
            );
        assertThat(newFilePath).exists();

        // 파일 존재 시 삭제
        if (Files.exists(newFilePath)) {
            try {
                Files.delete(newFilePath);
            } catch (IOException e) {
                throw e;
            }
        }
    }

}