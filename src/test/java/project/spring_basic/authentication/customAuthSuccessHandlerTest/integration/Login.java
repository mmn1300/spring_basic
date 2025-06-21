package project.spring_basic.authentication.customAuthSuccessHandlerTest.integration;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

import java.time.LocalDateTime;
import java.util.Objects;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.servlet.http.HttpSession;
import project.spring_basic.data.entity.Member;
import project.spring_basic.data.repository.MemberRepository;
import project.spring_basic.service.SessionService;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class Login {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private SessionService sessionService;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private PlatformTransactionManager transactionManager;



    // SessionService를 실제 Bean이 아닌 Mock Bean으로 대체
    @TestConfiguration
    static class MockServiceConfig {
        @Bean
        @Primary
        public SessionService sessionService() {
            return Mockito.mock(SessionService.class);
        }
    }



    @BeforeAll
	public void setUp(){
		String encodedPassword = bCryptPasswordEncoder.encode("tttttttt");
        Member member = Member.builder()
            .userId("tttttttt")
            .password(encodedPassword)
            .nickname("테스트용 임시 계정")
            .email("ttt@ttt.com")
            .phoneNumber("000-0000-0000")
            .createAt(LocalDateTime.now())
            .level(1)
            .build();

        memberRepository.saveAndFlush(member);
	}



    @AfterAll
	public void cleanUp(){
		// 트랜잭션 생성
        TransactionStatus status = transactionManager.getTransaction(new DefaultTransactionDefinition());

        try {
            // 모든 데이터 삭제
            memberRepository.deleteAllInBatch();

            // Auto Increment 값 초기화
            entityManager.createNativeQuery(
                "ALTER TABLE members ALTER COLUMN id RESTART WITH 1"
            ).executeUpdate();

            transactionManager.commit(status);
        } catch (Exception e) {
            transactionManager.rollback(status);
            throw e;
        }
	}



    @Test
    @DisplayName("로그인 시 사용자 정보로 세션을 생성하고 /board로 리다이렉트 시킨다.")
    public void login() throws Exception {
        // When & Then
        mockMvc.perform(post("/account/login")
                    .param("username", "tttttttt")
                    .param("password", "tttttttt")
                    .with(csrf())
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/board"))
                .andDo(result -> {
                    HttpSession session = result.getRequest().getSession(false);
                    assertThat(session).isNotNull();
                    verify(sessionService).createSession(eq(session), any(Member.class));
                });
    }



    @Test
    @DisplayName("CSRF 토큰이 존재하지 않는 요청이면 400번대 에러를 응답한다.")
    public void loginCsrfTokenNotExists() throws Exception {
        // When & Then
        mockMvc.perform(post("/account/login")
                    .param("username", "tttttttt")
                    .param("password", "tttttttt")
                )
                .andExpect(status().is4xxClientError());
    }



    @Test
    @DisplayName("동일 계정 로그인 시 기존 세션이 만료되고 새 세션이 생성된다.")
    public void duplicateLogin() throws Exception {
        // when & then
        // 로그인 1
        MvcResult firstLogin = mockMvc.perform(post("/account/login")
                    .param("username", "tttttttt")
                    .param("password", "tttttttt")
                    .with(csrf())
                )
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/board"))
            .andReturn();

        HttpSession sessionA = firstLogin.getRequest().getSession(false);
        assertThat(sessionA).isNotNull();

        // 로그인 2 (동일 계정)
        MvcResult secondLogin = mockMvc.perform(post("/account/login")
                    .param("username", "tttttttt")
                    .param("password", "tttttttt")
                    .with(csrf())
                )
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/board"))
            .andReturn();

        HttpSession sessionB = secondLogin.getRequest().getSession(false);
        assertThat(sessionB).isNotNull();

        assertThat(Objects.requireNonNull(sessionB).getId())
            .isNotEqualTo(Objects.requireNonNull(sessionA).getId());
        verify(sessionService, times(2)).createSession(any(), any());
    }
}
