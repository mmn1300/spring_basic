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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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


    @Operation(
        summary = "게시글 작성 form",
        description = "게시글을 작성할 수 있는 템플릿을 렌더링함",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "템플릿 렌더링 성공"
            ),
            @ApiResponse(
                responseCode = "303",
                description = "세션 미존재. login 템플릿으로 리다이렉트"
            )
        }
    )
    @GetMapping("/create")
    public ModelAndView create(HttpSession session) {
        return new ModelAndView(sessionService.getTemplateOrDefault(session, "write_post"));
    }



    @Operation(
        summary = "게시글 읽기 form",
        description = "게시글을 읽을 수 있는 템플릿을 렌더링함",
        parameters = {
            @Parameter(
                name = "postNum",
                description = "게시글의 정수 아이디 값",
                required = true,
                in = ParameterIn.PATH
            )
        },
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "템플릿 렌더링 성공"
            ),
            @ApiResponse(
                responseCode = "303",
                description = "세션 미존재. login 템플릿으로 리다이렉트"
            ),
            @ApiResponse(
                responseCode = "500",
                description = "서버 오류로 인한 실패. error 템플릿으로 리다이렉트"
            )
        }
    )
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



    @Operation(
        summary = "게시글 수정 form",
        description = "게시글을 수정할 수 있는 템플릿을 렌더링함",
        parameters = {
            @Parameter(
                name = "postNum",
                description = "게시글의 정수 아이디 값",
                required = true,
                in = ParameterIn.PATH
            )
        },
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "템플릿 렌더링 성공"
            ),
            @ApiResponse(
                responseCode = "303",
                description = "세션 미존재. login 템플릿으로 리다이렉트"
            ),
            @ApiResponse(
                responseCode = "500",
                description = "서버 오류로 인한 실패. error 템플릿으로 리다이렉트"
            )
        }
    )
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
