package project.spring_basic.controller.AccountRestControllerTest.unit;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import project.spring_basic.controller.AccountRestControllerTest.AccountRestControllerUnitTestSupport;

@Tag("unit")
@Tag("controller")
@Tag("controller-unit")
public class CheckIdTest extends AccountRestControllerUnitTestSupport {

    @Test
    @DisplayName("아이디를 가진 회원이 존재하면 {message:true, data:true}를 응답한다")
    public void checkId() throws Exception {
        // given
        when(memberService.memberExistsById("tttttttt")).thenReturn(true);

        // when & then
        mockMvc.perform(get("/account/tttttttt"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.data.message").value("true"))
                .andExpect(jsonPath("$.data.data").value("true"));
    }



    @Test
    @DisplayName("처리 중 오류가 발생하면 {message:false, error:에러 메세지}를 응답한다")
    public void checkIdWhenExceptionOccurs() throws Exception {
        // given
        when(memberService.memberExistsById("tttttttt"))
                .thenThrow(new RuntimeException("데이터베이스 오류 발생"));

        // when & then
        mockMvc.perform(get("/account/tttttttt"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.code").value(500))
                .andExpect(jsonPath("$.status").value("INTERNAL_SERVER_ERROR"))
                .andExpect(jsonPath("$.data.message").value("false"))
                .andExpect(jsonPath("$.data.error")
                        .value("데이터베이스 오류 발생"));
    }
}
