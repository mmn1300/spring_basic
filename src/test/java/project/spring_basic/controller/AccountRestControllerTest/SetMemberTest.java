package project.spring_basic.controller.AccountRestControllerTest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import project.spring_basic.controller.AccountRestController;
import project.spring_basic.data.dto.Request.MemberDTO;
import project.spring_basic.service.MemberService;
import project.spring_basic.service.SessionService;

@Tag("unit")
@WebMvcTest(controllers = AccountRestController.class)
public class SetMemberTest {
        
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private MemberService memberService;

    @MockitoBean
    private SessionService sessionService;


    

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
                .andExpect(jsonPath("$.message").value("true"));
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
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("false"))
                .andExpect(jsonPath("$.error")
                        .value("데이터베이스 오류 발생"));
    }



    @Test
    @DisplayName("잘못된 입력 데이터로 요청하면 400번 코드를 응답한다.")
    public void setMemberWhenBadRequest() throws Exception {
        // given
        String requestBody = """
            {
                "userId": "",
                "pw": "",
                "name": "",
                "email": "",
                "phone": ""
            }
            """;

        // when & then
        mockMvc.perform(post("/account/member")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestBody)
                )
                .andExpect(status().isBadRequest());
    }
}
