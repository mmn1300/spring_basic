package project.spring_basic.controller.AccountRestControllerTest.unit;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;

import project.spring_basic.api.controller.AccountRestController;
import project.spring_basic.controller.AccountRestControllerTest.AccountRestControllerUnitTestSupport;
import project.spring_basic.data.dto.Request.MemberDTO;

@Tag("unit")
@Tag("controller")
@Tag("controller-unit")
@WebMvcTest(controllers = AccountRestController.class)
public class SetMemberTest extends AccountRestControllerUnitTestSupport {

    @Test
    @DisplayName("회원 등록을 무사히 수행하면 {message:true}를 응답한다.")
    public void setMember() throws Exception {
        // given
        String requestBody = """
            {
                "userId": "tttttttt",
                "pw": "tttttttt",
                "name": "테스트용 임시 계정",
                "email": "ttt@ttt.com",
                "phone": "000-0000-0000"
            }
            """;

        Mockito.doNothing().when(memberService).save(Mockito.any(MemberDTO.class));

        // when & then
        mockMvc.perform(post("/account/member")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestBody)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.data.message").value("true"));
    }



    @Test
    @DisplayName("처리 중 오류가 발생하면 {message:false, error:에러 메세지}를 응답한다.")
    public void setMemberWhenExceptionOccurs() throws Exception {
        // given
        String requestBody = """
            {
                "userId": "tttttttt",
                "pw": "tttttttt",
                "name": "테스트용 임시 계정",
                "email": "ttt@ttt.com",
                "phone": "000-0000-0000"
            }
            """;

        Mockito.doThrow(new RuntimeException("데이터베이스 오류 발생"))
                .when(memberService).save(Mockito.any(MemberDTO.class));


        // when & then
        mockMvc.perform(post("/account/member")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestBody)
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
    public void setMemberWhenBadRequest() throws Exception {
        // given
        String requestBody = """
            {
                "userId": "t",
                "pw": "t",
                "name": "ㅌ",
                "email": "t",
                "phone": "0"
            }
            """;

        // when & then
        mockMvc.perform(post("/account/member")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestBody)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(400))
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.messages").isArray())
                .andExpect(jsonPath("$.messages").value(
                    Matchers.containsInAnyOrder(
                        "아이디의 길이는 8자 이상 15자 이하여야 합니다.",
                        "비밀번호의 길이는 8자 이상 15자 이하여야 합니다.",
                        "이메일의 길이는 3자 이상 80자 이하여야 합니다.",
                        "휴대전화 번호는 13자여야 합니다."
                    )
                ));
    }
}
