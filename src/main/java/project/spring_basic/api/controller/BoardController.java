package project.spring_basic.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

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
    public ModelAndView create(HttpSession session) {
        return new ModelAndView(sessionService.getTemplateOrDefault(session, "write_post"));
    }


    // 게시글 읽기 form
    @GetMapping("/show/{postNum}")
    public ModelAndView show(@PathVariable("postNum") Long postNum, HttpSession session,
                        RedirectAttributes redirectAttributes) {
        ModelAndView mav = new ModelAndView();
        try{
            mav.addObject("post", boardService.getReadPost(postNum));
        }catch(Exception e){
            RedirectView redirectView = new RedirectView("/error");
            redirectView.setStatusCode(HttpStatus.SEE_OTHER);
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return new ModelAndView(redirectView);
        }
        mav.setViewName(sessionService.getTemplateOrDefault(session, "read_post"));
        return mav;
    }


    // 게시글 수정 form
    @GetMapping("/edit/{postNum}")
    public ModelAndView edit(@PathVariable("postNum") Long postNum, HttpSession session,
                        RedirectAttributes redirectAttributes) {
        ModelAndView mav = new ModelAndView();
        try{
            mav.addObject("post", boardService.getUpdatePost(postNum));
        }catch(Exception e){
            RedirectView redirectView = new RedirectView("/error");
            redirectView.setStatusCode(HttpStatus.SEE_OTHER);
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return new ModelAndView(redirectView);
        }
        mav.setViewName(sessionService.getTemplateOrDefault(session, "update_post"));
        return mav;
    }
}
