package project.spring_basic.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class ViewController {
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
}
