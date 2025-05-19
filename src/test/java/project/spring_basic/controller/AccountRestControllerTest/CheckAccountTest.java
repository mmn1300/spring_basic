package project.spring_basic.controller.AccountRestControllerTest;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import project.spring_basic.controller.AccountRestController;
import project.spring_basic.service.MemberService;
import project.spring_basic.service.SessionService;

@Tag("unit")
@WebMvcTest(controllers = AccountRestController.class)
public class CheckAccountTest {
    
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private MemberService memberService;

    @MockitoBean
    private SessionService sessionService;



    @Test
    @DisplayName("아이디와 비밀번호를 가진 회원이 존재하면 {message:true, data:true}를 응답한다.")
    public void checkAccount() throws Exception {
        // given
        String requestBody = """
            {
                "id": "tttttttt",
                "pw": "tttttttt"
            }
            """;

        when(memberService.memberExists("tttttttt", "tttttttt")).thenReturn(true);

        // when & then
        mockMvc.perform(post("/account/check")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestBody)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("true"))
                .andExpect(jsonPath("$.data").value("true"));
    }



    @Test
    @DisplayName("처리 중 오류가 발생하면 {message:false, error:에러 메세지}를 응답한다.")
    public void checkAccountWhenExceptionOccurs() throws Exception {
        // given
        String requestBody = """
            {
                "id": "tttttttt",
                "pw": "tttttttt"
            }
            """;

        when(memberService.memberExists("tttttttt", "tttttttt"))
                .thenThrow(new RuntimeException("데이터베이스 오류 발생"));

        // when & then
        mockMvc.perform(post("/account/check")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestBody)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("false"))
                .andExpect(jsonPath("$.error")
                        .value("데이터베이스 오류 발생"));
    }



    @Test
    @DisplayName("잘못된 입력 데이터로 요청하면 400번 코드를 응답한다.")
    public void checkAccountWhenBadRequest() throws Exception {
        // given
        String requestBody = """
            {
                "id": "t",
                "pw": "t"
            }
            """;

        when(memberService.memberExists("tttttttt", "tttttttt"))
                .thenThrow(new RuntimeException("데이터베이스 오류 발생"));

        // when & then
        mockMvc.perform(post("/account/check")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestBody)
                )
                .andExpect(status().isBadRequest());
    }
}
