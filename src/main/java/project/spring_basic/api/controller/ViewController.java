package project.spring_basic.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpSession;
import project.spring_basic.data.dto.Response.ModelAttribute.OptionDTO;
import project.spring_basic.service.SessionService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Controller
public class ViewController {
    
    @Autowired
    private SessionService sessionService;

    private final Logger LOGGER = LoggerFactory.getLogger(ViewController.class);

    @GetMapping("/")
    public ModelAndView index() {
        LOGGER.info("사용자가 홈페이지에 접속하였습니다.");
        return new ModelAndView("index");
    }

    @GetMapping("/login")
    public ModelAndView login() {
        return new ModelAndView("login_form");
    }

    @GetMapping("/signup")
    public ModelAndView signup() {
        return new ModelAndView("create_account");
    }

    @GetMapping("/board")
    public ModelAndView board(HttpSession session,
                            @RequestParam(value = "user", required = false) Long userId) {
        ModelAndView mav = new ModelAndView();
        if(userId == null){
            mav.addObject("option", new OptionDTO("", null));
        }else{
            mav.addObject("option", sessionService.getUserOptions(session, userId));
        }
        mav.setViewName(sessionService.getTemplateOrDefault(session, "board"));;
        return mav;
    }
    
    @GetMapping("/error")
    public ModelAndView error(@ModelAttribute("error") String message) {
        ModelAndView mav = new ModelAndView("error");
        mav.addObject("error", message);
        return mav;
    }
}
