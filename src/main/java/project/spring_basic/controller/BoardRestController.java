package project.spring_basic.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;

import project.spring_basic.dto.Response.ErrorDTO;
import project.spring_basic.dto.Response.PostsDTO;
import project.spring_basic.dto.Response.ResponseDTO;
import project.spring_basic.service.BoardService;



@RestController
@RequestMapping("/board")
public class BoardRestController {

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
    


    // 게시글 생성
    // 게시글 읽기 데이터 응답
    // 게시글 수정 데이터 응답
    // 게시글 삭제
}
