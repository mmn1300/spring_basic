package project.spring_basic.controller.BoardRestControllerTest.unit;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import project.spring_basic.api.ApiResponse;
import project.spring_basic.controller.BoardRestControllerTest.BoardRestControllerUnitTestSupport;
import project.spring_basic.data.dto.Response.Json.ErrorDTO;
import project.spring_basic.exception.MemberNotFoundException;

@Tag("unit")
@Tag("controller")
@Tag("controller-unit")
public class GetCountTest extends BoardRestControllerUnitTestSupport {

    @Test
    @DisplayName("사용자가 작성한 총 게시글의 수와 정상 처리 유무를 응답한다.")
    public void getCount() throws Exception {
        // given
        when(boardService.getUserPostCount("tttttttt")).thenReturn(1);


        // when & then
        mockMvc.perform(get("/board/tttttttt/posts/count"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.data.message").value(true))
                .andExpect(jsonPath("$.data.num").value(1));
    }



    @Test
    @DisplayName("처리 중 오류가 발생하면 {message:false, error:에러 메세지}를 응답한다.")
    public void getCountWhenExceptionOccurs() throws Exception {
        // given
        when(boardService.getUserPostCount("tttttttt"))
            .thenThrow(new MemberNotFoundException("해당 회원은 존재하지 않습니다."));

        ErrorDTO errorDTO = new ErrorDTO(false, "해당 회원은 존재하지 않습니다.");
        ApiResponse<ErrorDTO> apiResponse = new ApiResponse<ErrorDTO>(HttpStatus.INTERNAL_SERVER_ERROR, null, errorDTO);
        String ResponseJson = objectMapper.writeValueAsString(apiResponse); // 객체 -> json 문자열


        // when & then
        mockMvc.perform(get("/board/tttttttt/posts/count"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().json(ResponseJson));
    }
}
