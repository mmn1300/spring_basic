package project.spring_basic.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import project.spring_basic.data.dto.Request.PostDTO;
import project.spring_basic.data.dto.Response.Json.BooleanDTO;
import project.spring_basic.data.dto.Response.Json.ErrorDTO;
import project.spring_basic.data.dto.Response.Json.FileNameDTO;
import project.spring_basic.data.dto.Response.Json.PostsDTO;
import project.spring_basic.data.dto.Response.Json.ResponseDTO;
import project.spring_basic.service.BoardService;
import project.spring_basic.service.SessionService;




@RestController
@RequestMapping("/board")
public class BoardRestController {

    @Autowired
    private SessionService sessionService;

    @Autowired
    private BoardService boardService;

    // 게시글 조회
    @GetMapping("/{pageNum}")
    public ResponseDTO getPosts(@PathVariable("pageNum") int pageNum) throws Exception{
        PostsDTO postsDTO = new PostsDTO(false, 0, null);
        try{
            postsDTO = boardService.getPostsInfo(pageNum);
        }catch(Exception e){
            return new ErrorDTO(false, e.getMessage());
        }
        return postsDTO;
    }
    

    // 게시글 저장
    @PostMapping("/store")
    public ResponseDTO store(@ModelAttribute @Valid PostDTO postDTO,
                             @RequestParam(value="file", required=false) MultipartFile file,
                             HttpSession session) {
        try{
            boardService.save(postDTO, sessionService.getId(session), file);
        }catch(Exception e){
            return new ErrorDTO(false, e.getMessage());
        }
        return new ResponseDTO(true);
    }
    

    // 게시글 파일 데이터 응답
    @GetMapping("/file/{postNum}")
    public ResponseDTO isFileExists(@PathVariable("postNum") Long postNum) {
        try{
            String result = boardService.isFileExists(postNum);
            return new FileNameDTO(true, result);
        }catch(Exception e){
            return new ErrorDTO(false, e.getMessage());
        }
    }
    

    // 게시글 파일 다운로드
    @GetMapping("/download/{postNum}")
    public ResponseEntity<?> fileDownload(@PathVariable("postNum") Long postNum) {
        try{
            return boardService.getFile(postNum);
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }


    // 게시글 수정 데이터 응답
    @PutMapping("/update/{postNum}")
    public ResponseDTO update(@PathVariable("postNum") Long postNum, @Valid PostDTO postDTO,
                              @RequestParam(value="file", required=false) MultipartFile file) {
        try{
            boardService.update(postNum, postDTO, file);
        }catch(Exception e){
            return new ErrorDTO(false, e.getMessage());
        }
        return new ResponseDTO(true);
    }

    // 게시글 삭제
    @DeleteMapping("/remove/{postNum}")
    public ResponseDTO removePost(@PathVariable("postNum") Long postNum){
        try{
            boardService.remove(postNum);
        }catch(Exception e){
            return new ErrorDTO(false, e.getMessage());
        }
        return new ResponseDTO(true);
    }


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
