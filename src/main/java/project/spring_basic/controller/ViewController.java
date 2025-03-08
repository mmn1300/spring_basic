package project.spring_basic.controller;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.servlet.http.HttpSession;
import project.spring_basic.service.SessionService;



@Controller
public class ViewController {
    
    @Autowired
    private SessionService sessionService;

    @GetMapping("/")
    public String index() {
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
    public String board(HttpSession session) {
        return sessionService.getTemplateOrDefault(session, "board");
    }
    
    @GetMapping("/error")
    public String error() {
        return "error";
    }
}
