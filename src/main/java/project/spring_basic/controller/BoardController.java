package project.spring_basic.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.ui.Model;

import jakarta.servlet.http.HttpSession;
import project.spring_basic.service.BoardService;
import project.spring_basic.service.SessionService;

@Controller
@RequestMapping("/board")
public class BoardController {

    @Autowired
    private SessionService sessionService;

    @Autowired
    private BoardService boardService;


    @GetMapping("/store")
    public String store(HttpSession session) {
        return sessionService.getTemplateOrDefault(session, "write_post");
    }

    @GetMapping("/show/{postNum}")
    public String show(@PathVariable("postNum") Long postNum, HttpSession session, Model model,
                        RedirectAttributes redirectAttributes) {
        model.addAttribute("number", postNum);
        model.addAttribute("nickname", sessionService.getNickname(session));
        try{
            model.addAttribute("post", boardService.getPost(postNum));
        }catch(Exception e){
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/error";
        }
        return sessionService.getTemplateOrDefault(session, "read_post");
    }

    @GetMapping("/edit")
    public String edit(HttpSession session) {
        return sessionService.getTemplateOrDefault(session, "update_post");
    }
}
