package project.spring_basic.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.PathVariable;

import project.spring_basic.dto.Request.PostDTO;
import project.spring_basic.dto.Response.BooleanDTO;
import project.spring_basic.dto.Response.ErrorDTO;
import project.spring_basic.dto.Response.PostsDTO;
import project.spring_basic.dto.Response.ResponseDTO;
import project.spring_basic.service.BoardService;
import project.spring_basic.service.SessionService;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;




@RestController
@RequestMapping("/board")
public class BoardRestController {

    @Autowired
    private SessionService sessionService;

    @Autowired
    private BoardService boardService;

    // 게시글 조회
    @GetMapping("/{pageNum}")
    public ResponseDTO getPosts(@PathVariable("pageNum") Long pageNum) throws Exception{
        PostsDTO postsDTO = new PostsDTO(false, 0, null);
        try{
            postsDTO = boardService.getPosts(postsDTO, pageNum);
        }catch(Exception e){
            return new ErrorDTO(false, e.getMessage());
        }
        return postsDTO;
    }
    

    // 게시글 저장
    @PostMapping("/store")
    public ResponseDTO store(@RequestBody PostDTO postDTO, HttpSession session) {
        try{
            boardService.save(postDTO, sessionService.getUserId(session), sessionService.getNickname(session));
        }catch(Exception e){
            return new ErrorDTO(false, e.getMessage());
        }
        return new ResponseDTO(true);
    }
    

    // 게시글 파일 데이터 응답

    // 게시글 수정 데이터 응답

    // 게시글 삭제

    // 게시글 작성자 응답
    @GetMapping("/user/{postNum}")
    public ResponseDTO checPostUser(@PathVariable("postNum") Long postNum ,HttpSession session){
        boolean result = false;
        try{
            result = boardService.checkUser(postNum, sessionService.getUserId(session));
        }catch(Exception e){
            return new ErrorDTO(false, e.getMessage());
        }
        return new BooleanDTO(true, result);
    }
}
