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


    // 게시글 작성 form
    @GetMapping("/create")
    public String create(HttpSession session) {
        return sessionService.getTemplateOrDefault(session, "write_post");
    }


    // 게시글 읽기 form
    @GetMapping("/show/{postNum}")
    public String show(@PathVariable("postNum") Long postNum, HttpSession session, Model model,
                        RedirectAttributes redirectAttributes) {
        try{
            model.addAttribute("post", boardService.getReadPost(postNum));
        }catch(Exception e){
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/error";
        }
        return sessionService.getTemplateOrDefault(session, "read_post");
    }


    // 게시글 수정 form
    @GetMapping("/edit/{postNum}")
    public String edit(@PathVariable("postNum") Long postNum, HttpSession session, Model model,
                        RedirectAttributes redirectAttributes) {
        try{
            model.addAttribute("post", boardService.getUpdatePost(postNum));
        }catch(Exception e){
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/error";
        }
        return sessionService.getTemplateOrDefault(session, "update_post");
    }
}
