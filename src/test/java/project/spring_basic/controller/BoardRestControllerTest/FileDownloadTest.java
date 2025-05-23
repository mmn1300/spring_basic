package project.spring_basic.controller.BoardRestControllerTest;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import project.spring_basic.api.controller.BoardRestController;
import project.spring_basic.constant.UserDefinePath;
import project.spring_basic.exception.PostNotFoundException;
import project.spring_basic.service.BoardService;
import project.spring_basic.service.SessionService;

@Tag("unit")
@Tag("controller")
@Tag("controller-unit")
@WebMvcTest(controllers = BoardRestController.class)
public class FileDownloadTest {
        
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private SessionService sessionService;

    @MockitoBean
    private BoardService boardService;



    // 매 테스트 메서드 종료 시 자동 실행
    @AfterEach
    public void tearDown() throws Exception{
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
    @DisplayName("파일 리소스를 응답한다.")
    public void fileDownload() throws Exception {
        // given
        String tempName = "test.txt";
        String uploadDir = UserDefinePath.ABS_PATH + UserDefinePath.FILE_STORAGE_PATH;
        File targetFile = new File(uploadDir, tempName);
        if (!targetFile.exists()) {
            targetFile.createNewFile();
        }

        Path path = Paths.get(uploadDir + '\\' + tempName);
        byte[] expectedBytes = Files.readAllBytes(path);
        
        Resource resource = new FileSystemResource(path);
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + tempName);
        headers.add(HttpHeaders.CONTENT_TYPE, "application/octet-stream");

        when(boardService.getFile(1L)).thenReturn(
                    ResponseEntity.ok()
                    .headers(headers)
                    .body(resource)
                );


        // when & then
        mockMvc.perform(get("/board/download/1"))
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + tempName))
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, "application/octet-stream"))
                .andExpect(content().bytes(expectedBytes));
    }



    @Test
    @DisplayName("처리 중 오류가 발생하면 INTERNAL_SERVER_ERROR와 오류 메세지를 응답한다.")
    public void fileDownloadWhenExceptionOccurs() throws Exception {
        // given
        when(boardService.getFile(1L))
                .thenThrow(new PostNotFoundException("1번 게시글은 존재하지 않습니다."));

        // when & then
        mockMvc.perform(get("/board/download/1"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Error: 1번 게시글은 존재하지 않습니다."));
    }
}
