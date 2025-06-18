package project.spring_basic.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

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

    @GetMapping("/info")
    public ModelAndView info(HttpSession session, RedirectAttributes redirectAttributes) {
        ModelAndView mav = new ModelAndView();
        try {
            Long id = sessionService.getId(session);
            if (id > 0L) {
                mav.addObject("accountInfo", memberService.getAccountInfo(id));
                mav.setStatus(HttpStatus.OK);
            }else{
                mav.setStatus(HttpStatus.SEE_OTHER);
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            RedirectView redirectView = new RedirectView("/error");
            redirectView.setStatusCode(HttpStatus.SEE_OTHER);
            return new ModelAndView(redirectView);
        }
        mav.setViewName(sessionService.getTemplateOrDefault(session, "my_page"));
        return mav;
    }
}
