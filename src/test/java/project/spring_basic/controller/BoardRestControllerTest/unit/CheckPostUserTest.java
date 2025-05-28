package project.spring_basic.controller.BoardRestControllerTest.unit;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpSession;

import project.spring_basic.api.ApiResponse;
import project.spring_basic.controller.BoardRestControllerTest.BoardRestControllerUnitTestSupport;
import project.spring_basic.data.dto.Response.Json.BooleanDTO;
import project.spring_basic.data.dto.Response.Json.ErrorDTO;
import project.spring_basic.exception.MemberNotFoundException;

@Tag("unit")
@Tag("controller")
@Tag("controller-unit")
public class CheckPostUserTest extends BoardRestControllerUnitTestSupport {

    @Test
    @DisplayName("정상처리 유무와 게시글의 작성자 일치 여부를 응답한다.")
    public void checkPostUser() throws Exception {
        // given
        MockHttpSession session = new MockHttpSession();

        when(sessionService.getUserId(session)).thenReturn("tttttttt");
        when(boardService.checkUser(1L, "tttttttt")).thenReturn(true);

        BooleanDTO booleanDTO = new BooleanDTO(true, true);
        ApiResponse<BooleanDTO> apiResponse = new ApiResponse<BooleanDTO>(HttpStatus.OK, null, booleanDTO);
        String ResponseJson = objectMapper.writeValueAsString(apiResponse);


        // when & then
        mockMvc.perform(get("/board/user/1")
                    .session(session)
                )
                .andExpect(status().isOk())
                .andExpect(content().json(ResponseJson));
    }



    @Test
    @DisplayName("처리 중 오류가 발생하면 {message:false, error:에러 메세지}를 응답한다.")
    public void checkPostUserWhenExceptionOccurs() throws Exception {
        // given
        MockHttpSession session = new MockHttpSession();

        when(sessionService.getUserId(session)).thenReturn("tttttttt");
        when(boardService.checkUser(1L, "tttttttt"))
                .thenThrow(new MemberNotFoundException("해당 회원은 존재하지 않습니다."));

        ErrorDTO errorDTO = new ErrorDTO(false, "해당 회원은 존재하지 않습니다.");
        ApiResponse<ErrorDTO> apiResponse = new ApiResponse<ErrorDTO>(HttpStatus.INTERNAL_SERVER_ERROR, null, errorDTO);
        String ResponseJson = objectMapper.writeValueAsString(apiResponse);


        // when & then
        mockMvc.perform(get("/board/user/1")
                    .session(session)
                )
                .andExpect(status().isInternalServerError())
                .andExpect(content().json(ResponseJson));
    }
}
