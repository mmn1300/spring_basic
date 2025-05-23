package project.spring_basic.controller.BoardRestControllerTest;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import project.spring_basic.api.ApiResponse;
import project.spring_basic.api.controller.BoardRestController;
import project.spring_basic.data.PostInfo;
import project.spring_basic.data.dto.Response.Json.ErrorDTO;
import project.spring_basic.data.dto.Response.Json.PostsDTO;
import project.spring_basic.data.dto.Response.Json.ResponseDTO;
import project.spring_basic.exception.MemberNotFoundException;
import project.spring_basic.service.BoardService;
import project.spring_basic.service.SessionService;

@Tag("unit")
@Tag("controller")
@Tag("controller-unit")
@WebMvcTest(controllers = BoardRestController.class)
public class GetPostsByUserTest {
    
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private SessionService sessionService;

    @MockitoBean
    private BoardService boardService;



    @Test
    @DisplayName("페이지에 해당하는 게시글들과 게시글의 수, 정상처리 유무를 응답한다.")
    public void getPostsByUser() throws Exception {
        // given
        List<PostInfo> posts = new ArrayList<>();
        for (int i=1; i<15; i++){
            PostInfo postInfo = new PostInfo(
                Long.valueOf(i),
                "tttttttt",
                "테스트용 임시 계정",
                Integer.toString(i),
                Integer.toString(i), 
                "2000-01-01 00:00:" + String.format("%02d", i)
            );
            posts.add(postInfo);
        }
        PostsDTO postsDTO = new PostsDTO(true, 14, posts);
        ApiResponse<PostsDTO> apiResponse = new ApiResponse<PostsDTO>(HttpStatus.OK, null, postsDTO);

        when(boardService.getPostsInfoByUser(0, 1L)).thenReturn(postsDTO);

        String ResponseJson = objectMapper.writeValueAsString(apiResponse); // 객체 -> json 문자열


        // when & then
        mockMvc.perform(get("/board/posts?page=0&user=1"))
                .andExpect(status().isOk())
                .andExpect(content().json(ResponseJson));
    }



    @Test
    @DisplayName("처리 중 오류가 발생하면 {message:false, error:에러 메세지}를 응답한다.")
    public void getPostsByUserWhenExceptionOccurs() throws Exception {
        // given
        when(boardService.getPostsInfoByUser(0, 1L))
            .thenThrow(new MemberNotFoundException("해당 회원은 존재하지 않습니다."));

        ErrorDTO errorDTO = new ErrorDTO(false, "해당 회원은 존재하지 않습니다.");
        ApiResponse<ResponseDTO> apiResponse = new ApiResponse<ResponseDTO>(HttpStatus.INTERNAL_SERVER_ERROR, null, errorDTO);
        String ResponseJson = objectMapper.writeValueAsString(apiResponse); // 객체 -> json 문자열


        // when & then
        mockMvc.perform(get("/board/posts?page=0&user=1"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().json(ResponseJson));
    }
}
