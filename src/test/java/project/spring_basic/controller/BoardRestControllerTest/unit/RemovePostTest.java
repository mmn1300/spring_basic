package project.spring_basic.controller.BoardRestControllerTest.unit;

import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;

import project.spring_basic.api.ApiResponse;
import project.spring_basic.controller.BoardRestControllerTest.BoardRestControllerUnitTestSupport;
import project.spring_basic.data.dto.Response.Json.ErrorDTO;

@Tag("unit")
@Tag("controller")
@Tag("controller-unit")
public class RemovePostTest extends BoardRestControllerUnitTestSupport {

    @Test
    @DisplayName("정상처리 유무를 응답한다.")
    public void removePost() throws Exception {
        // given
        Mockito.doNothing().when(boardService)
                .remove(eq(1L));


        // when & then
        mockMvc.perform(delete("/board/post/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.data.message").value(true));
    }



    @Test
    @DisplayName("처리 중 오류가 발생하면 {message:false, error:에러 메세지}를 응답한다.")
    public void removePostWhenExceptionOccurs() throws Exception {
        // given
        Mockito.doThrow(new RuntimeException("데이터베이스 접근 오류"))
                .when(boardService)
                .remove(eq(1L));

        ErrorDTO errorDTO = new ErrorDTO(false, "데이터베이스 접근 오류");
        ApiResponse<ErrorDTO> apiResponse = new ApiResponse<ErrorDTO>(HttpStatus.INTERNAL_SERVER_ERROR, null, errorDTO);
        String ResponseJson = objectMapper.writeValueAsString(apiResponse); // 객체 -> json 문자열


        // when & then
        mockMvc.perform(delete("/board/post/1"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().json(ResponseJson));
    }

}
