package project.spring_basic.controller;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
    public String index() {
        LOGGER.info("사용자가 홈페이지에 접속하였습니다.");
        return "index";
    }

    @GetMapping("/login")
    public String login() {
        return "login_form";
    }

    @GetMapping("/signup")
    public String signup() {
        return "create_account";
    }

    @GetMapping("/board")
    public String board(HttpSession session, Model model,
                        @RequestParam(value = "user", required = false) Long userId) {
        if(userId == null){
            model.addAttribute("option", new OptionDTO("", null));
        }else{
            model.addAttribute("option", sessionService.getUserOptions(session, userId));
        }
        return sessionService.getTemplateOrDefault(session, "board");
    }
    
    @GetMapping("/error")
    public String error() {
        return "error";
    }
}
