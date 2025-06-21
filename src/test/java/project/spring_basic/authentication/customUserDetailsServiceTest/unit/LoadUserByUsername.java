package project.spring_basic.authentication.customUserDetailsServiceTest.unit;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import project.spring_basic.api.custom.CustomDetailsService.CustomUserDetailsService;
import project.spring_basic.data.entity.Member;
import project.spring_basic.service.MemberService;


@ActiveProfiles("test")
public class LoadUserByUsername {
    
    @InjectMocks
    private CustomUserDetailsService customUserDetailsService;

    @Mock
    private MemberService memberService;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;



    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        bCryptPasswordEncoder = new BCryptPasswordEncoder();
    }



    @Test
    @DisplayName("사용자 정보를 조회한다.")
    void loadUserByUsername() throws Exception {
        // given
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

        when(memberService.getMemberByUserId("tttttttt")).thenReturn(member);

        // when
        UserDetails userDetails = customUserDetailsService.loadUserByUsername("tttttttt");

        // then
        assertThat(userDetails).isNotNull();
        assertThat(userDetails.getUsername()).isEqualTo("tttttttt");
        assertThat(userDetails.getPassword()).isEqualTo(encodedPassword);
        assertThat(userDetails.getAuthorities())
            .extracting("authority")
            .contains("ROLE_USER");
    }



    @Test
    @DisplayName("존재하지 않는 사용자 정보를 조회하는 경우 예외를 발생시킨다.")
    void loadUserByUsernameWhenUnknown() throws Exception {
        // given
        when(memberService.getMemberByUserId("unknown")).thenReturn(null);

        // when & then
        assertThatThrownBy(() -> customUserDetailsService.loadUserByUsername("unknown"))
            .isInstanceOf(UsernameNotFoundException.class)
            .hasMessage("사용자를 찾을 수 없습니다.");
    }



    @Test
    @DisplayName("사용자 정보 조회중 예외가 발생하면 UsernameNotFoundException 형태로 예외를 발생시킨다.")
    void loadUserByUsernameWhenExceptionOccurs() throws Exception {
        // given
        when(memberService.getMemberByUserId("errorUser"))
            .thenThrow(new RuntimeException("DB 오류"));

        // when & then
        assertThatThrownBy(() -> customUserDetailsService.loadUserByUsername("errorUser"))
            .isInstanceOf(UsernameNotFoundException.class)
            .hasMessage("회원 조회 중 오류가 발생하였습니다.")
            .hasCauseInstanceOf(RuntimeException.class)
            .cause()
            .hasMessage("DB 오류");
    }
}
