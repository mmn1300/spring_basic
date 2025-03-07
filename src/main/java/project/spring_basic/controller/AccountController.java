package project.spring_basic.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpSession;
import project.spring_basic.service.MemberService;
import project.spring_basic.service.SessionService;


@RequestMapping("/account")
@Controller
public class AccountController {

    @Autowired
    private MemberService memberService;

    @Autowired
    private SessionService sessionService;
    

    // 로그인
    @PostMapping("/login")
    public String login(String id, String pw, HttpSession session,
                        RedirectAttributes redirectAttributes) {
        // 세션 생성 후 게시판으로 리다이렉트
        try{
            sessionService.createSessionfromUserId(session, memberService.getMemberInfo(id));
        }catch(Exception e){
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/error";
        }
        return "redirect:/board";
    }
}
