package project.spring_basic.service.BoardServiceTest.integration;

import static org.assertj.core.api.Assertions.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import project.spring_basic.constant.UserDefinePath;
import project.spring_basic.data.dto.Request.PostDTO;
import project.spring_basic.data.entity.Post;

import project.spring_basic.service.BoardServiceTest.BoardServiceIntegrationTestSupport;

@Tag("integration")
@Tag("service")
@Tag("service-integration")
@Tag("BoardService")
@Tag("BoardService-integration")
public class SaveTest extends BoardServiceIntegrationTestSupport {

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

}
