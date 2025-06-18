package project.spring_basic.api.CustomHandler;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import project.spring_basic.data.entity.Member;
import project.spring_basic.service.SessionService;
import project.spring_basic.service.querys.MemberServiceQuerys;

@Component
public class CustomAuthSuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    private MemberServiceQuerys memberServiceQuerys;

    @Autowired
    private SessionService sessionService;

    
    // 로그인 성공 시 처리
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        String userId = authentication.getName();
        Member member = null;
        HttpSession session = request.getSession(true);
        System.out.println("/account/login 세션 ID: " + session.getId());

        try{
            member = memberServiceQuerys.getMemberByUserId(userId);
            sessionService.createSession(session, member);
        }catch(Exception e){
            throw new IOException(e.getMessage(), e);
        }

        response.sendRedirect("/board");
    }
}
