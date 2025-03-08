package project.spring_basic.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpSession;
import project.spring_basic.service.SessionService;

@RequestMapping("/board")
public class BoardController {

    @Autowired
    private SessionService sessionService;

    @GetMapping("/write")
    public String writePost(HttpSession session) {
        return sessionService.getTemplateOrDefault(session, "write_post");
    }

    @GetMapping("/read")
    public String readPost(HttpSession session) {
        return sessionService.getTemplateOrDefault(session, "read_post");
    }

    @GetMapping("/edit")
    public String updatePost(HttpSession session) {
        return sessionService.getTemplateOrDefault(session, "update_post");
    }
}
