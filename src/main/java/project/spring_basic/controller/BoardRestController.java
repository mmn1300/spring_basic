package project.spring_basic.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;


@RestController
@RequestMapping("/board")
public class BoardRestController {

    // 게시글 조회
    @GetMapping("/posts")
    public String getPosts() {
        return "";
    }
    


    // 게시글 생성
    // 게시글 읽기 데이터 응답
    // 게시글 수정 데이터 응답
    // 게시글 삭제
}
