package project.spring_basic.api.CustomDetailsService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import project.spring_basic.data.entity.Member;
import project.spring_basic.service.MemberService;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    
    @Autowired
    private MemberService memberService;

    // 로그인 시도 시 UserDetails 객체 생성
    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        Member member = null;
        try{
            member = memberService.getMemberByUserId(userId);
        }catch(Exception e){
            throw new UsernameNotFoundException("회원 조회 중 오류가 발생하였습니다.", e);
        }

        if (member == null) {
            throw new UsernameNotFoundException("사용자를 찾을 수 없습니다.");
        }

        return User.builder()
            .username(member.getUserId())
            .password(member.getPassword())
            .roles("USER")
            .build();
    }
}
