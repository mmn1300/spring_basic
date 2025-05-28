package project.spring_basic.controller.AccountRestControllerTest.unit;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;

import project.spring_basic.api.controller.AccountRestController;
import project.spring_basic.controller.AccountRestControllerTest.AccountRestControllerUnitTestSupport;
import project.spring_basic.data.dto.Request.NewAccountDTO;

@Tag("unit")
@Tag("controller")
@Tag("controller-unit")
@WebMvcTest(controllers = AccountRestController.class)
public class UpdateAccountTest extends AccountRestControllerUnitTestSupport {

    @Test
    @DisplayName("회원 등록을 무사히 수행하면 {message:true}를 응답한다.")
    public void updateAccount() throws Exception {
        // given
        MockHttpSession session = new MockHttpSession();

        String requestBody = """
            {
                "userId": "tttttttt",
                "nickname": "테스트용 임시 계정",
                "email": "ttt@ttt.com",
                "phone": "000-0000-0000"
            }
            """;

        when(sessionService.getId(session)).thenReturn(1L);

        Mockito.doNothing().when(memberService)
                .update(Mockito.any(NewAccountDTO.class), Mockito.anyLong());



        // when & then
        mockMvc.perform(put("/account/tttttttt")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestBody)
                    .session(session)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.data.message").value("true"));
    }



    @Test
    @DisplayName("처리 중 오류가 발생하면 {message:false, error:에러 메세지}를 응답한다.")
    public void updateAccountWhenExceptionOccurs() throws Exception {
        // given
        MockHttpSession session = new MockHttpSession();

        String requestBody = """
            {
                "userId": "tttttttt",
                "nickname": "테스트용 임시 계정",
                "email": "ttt@ttt.com",
                "phone": "000-0000-0000"
            }
            """;

        when(sessionService.getId(session)).thenReturn(1L);

        Mockito.doThrow(new RuntimeException("데이터베이스 오류 발생"))
                .when(memberService).update(Mockito.any(NewAccountDTO.class), Mockito.anyLong());


        // when & then
        mockMvc.perform(put("/account/tttttttt")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestBody)
                    .session(session)
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
    public void updateAccountrWhenBadRequest() throws Exception {
        // given
        MockHttpSession session = new MockHttpSession();

        String requestBody = """
            {
                "userId": null,
                "nickname": "ㅌ",
                "email": "t",
                "phone": "t"
            }
            """;

        // when & then
        mockMvc.perform(put("/account/tttttttt")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestBody)
                    .session(session)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(400))
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.messages").isArray())
                .andExpect(jsonPath("$.messages").value(
                    Matchers.containsInAnyOrder(
                        "아이디가 존재해야 합니다.",
                        "이메일의 길이는 3자 이상 80자 이하여야 합니다.",
                        "휴대전화 번호는 13자여야 합니다."
                    )
                ));
    }
}
