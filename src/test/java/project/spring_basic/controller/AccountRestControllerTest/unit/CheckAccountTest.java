package project.spring_basic.controller.AccountRestControllerTest.unit;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;


import project.spring_basic.controller.AccountRestControllerTest.AccountRestControllerUnitTestSupport;

@Tag("unit")
@Tag("controller")
@Tag("controller-unit")
public class CheckAccountTest extends AccountRestControllerUnitTestSupport {

    @Test
    @DisplayName("아이디와 비밀번호를 가진 회원이 존재하면 {message:true, data:true}를 응답한다.")
    public void checkAccount() throws Exception {
        // given
        String requestBody = """
            {
                "username": "tttttttt",
                "password": "tttttttt"
            }
            """;

        when(memberService.memberExists("tttttttt", "tttttttt")).thenReturn(true);


        // when & then
        mockMvc.perform(post("/account/check")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestBody)
                    .with(csrf())
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.data.message").value("true"))
                .andExpect(jsonPath("$.data.data").value("true"));
    }



    @Test
    @DisplayName("CSRF 토큰이 존재하지 않는 요청이면 403에러를 응답한다.")
    public void checkAccountCsrfTokenNotExists() throws Exception {
        // given
        String requestBody = """
            {
                "username": "tttttttt",
                "password": "tttttttt"
            }
            """;

        when(memberService.memberExists("tttttttt", "tttttttt")).thenReturn(true);

        // when & then
        mockMvc.perform(post("/account/check")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestBody)
                )
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.code").value(403))
                .andExpect(jsonPath("$.status").value("FORBIDDEN"))
                .andExpect(jsonPath("$.data.message").value("false"));
    }



    @Test
    @DisplayName("처리 중 오류가 발생하면 {message:false, error:에러 메세지}를 응답한다.")
    public void checkAccountWhenExceptionOccurs() throws Exception {
        // given
        String requestBody = """
            {
                "username": "tttttttt",
                "password": "tttttttt"
            }
            """;

        when(memberService.memberExists("tttttttt", "tttttttt"))
                .thenThrow(new RuntimeException("데이터베이스 오류 발생"));

        // when & then
        mockMvc.perform(post("/account/check")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestBody)
                    .with(csrf())
                )
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.code").value(500))
                .andExpect(jsonPath("$.status").value("INTERNAL_SERVER_ERROR"))
                .andExpect(jsonPath("$.data.message").value("false"))
                .andExpect(jsonPath("$.data.error")
                        .value("데이터베이스 오류 발생"));
    }



    @Test
    @DisplayName("잘못된 입력 데이터로 요청하면 400번 코드를 응답한다.")
    public void checkAccountWhenBadRequest() throws Exception {
        // given
        String requestBody = """
            {
                "username": "t",
                "password": "t"
            }
            """;

        when(memberService.memberExists("tttttttt", "tttttttt"))
                .thenThrow(new RuntimeException("데이터베이스 오류 발생"));

        // when & then
        mockMvc.perform(post("/account/check")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestBody)
                    .with(csrf())
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(400))
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.messages").isArray())
                .andExpect(jsonPath("$.messages").value(
                    Matchers.containsInAnyOrder(
                        "아이디의 길이는 8자 이상 15자 이하여야 합니다.",
                        "비밀번호의 길이는 8자 이상 15자 이하여야 합니다."
                    )
                ));
    }
}
