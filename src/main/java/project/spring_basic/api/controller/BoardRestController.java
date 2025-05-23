package project.spring_basic.api.controller;

// import org.slf4j.Logger;
// import org.slf4j.LoggerFactory;

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

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import project.spring_basic.api.ApiResponse;
import project.spring_basic.data.dto.Request.PostDTO;
import project.spring_basic.data.dto.Response.Json.BooleanDTO;
import project.spring_basic.data.dto.Response.Json.FileNameDTO;
import project.spring_basic.data.dto.Response.Json.NumberDTO;
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

    // private final Logger LOGGER = LoggerFactory.getLogger(ViewController.class);

    // 게시글 조회
    @GetMapping(value = "/posts", params = {"page", "!user"})
    public ResponseEntity<ApiResponse<ResponseDTO>> getPosts(HttpServletRequest request) throws Exception{
        int pageNum = Integer.parseInt(request.getParameter("page"));
        try{
            return ResponseEntity.ok(ApiResponse.ok(boardService.getPostsInfo(pageNum)));
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                .body(ApiResponse.internalServerError(e.getMessage()));
        }
    }


    // 게시글 조회(유저 별)
    @GetMapping(value = "/posts", params = {"page", "user"})
    public ResponseEntity<ApiResponse<ResponseDTO>> getPostsByUser(HttpServletRequest request) throws Exception{
        int pageNum = Integer.parseInt(request.getParameter("page"));
        Long userId = Long.parseLong(request.getParameter("user"));
        try{
            return ResponseEntity.ok(ApiResponse.ok(boardService.getPostsInfoByUser(pageNum, userId)));
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                .body(ApiResponse.internalServerError(e.getMessage()));
        }
    }


    // 내 게시글 수
    @GetMapping("/{userId}/posts/count")
    public ResponseEntity<ApiResponse<ResponseDTO>> getCount(@PathVariable("userId") String userId) throws Exception{
        try{
            return ResponseEntity.ok(ApiResponse.ok(new NumberDTO(true, boardService.getUserPostCount(userId))));
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                .body(ApiResponse.internalServerError(e.getMessage()));
        }
    }
    

    // 게시글 저장
    @PostMapping("/post")
    public ResponseEntity<ApiResponse<ResponseDTO>> storePost(@ModelAttribute @Valid PostDTO postDTO,
                             @RequestParam(value="file", required=false) MultipartFile file,
                             HttpSession session) {
        try{
            boardService.save(postDTO, sessionService.getId(session), file);
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                .body(ApiResponse.internalServerError(e.getMessage()));
        }
        return ResponseEntity.ok(ApiResponse.ok(new ResponseDTO(true)));
    }
    

    // 게시글 파일 데이터 응답
    @GetMapping("/file/{postNum}")
    public ResponseEntity<ApiResponse<ResponseDTO>> getFileName(@PathVariable("postNum") Long postNum) {
        try{
            String result = boardService.isFileExists(postNum);
            return ResponseEntity.ok(ApiResponse.ok(new FileNameDTO(true, result)));
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                .body(ApiResponse.internalServerError(e.getMessage()));
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
    @PutMapping("/post/{postNum}")
    public ResponseEntity<ApiResponse<ResponseDTO>> updatePost(@PathVariable("postNum") Long postNum, @Valid PostDTO postDTO,
                              @RequestParam(value="file", required=false) MultipartFile file) {
        try{
            boardService.update(postNum, postDTO, file);
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                .body(ApiResponse.internalServerError(e.getMessage()));
        }
        return ResponseEntity.ok(ApiResponse.ok(new ResponseDTO(true)));
    }

    // 게시글 삭제
    @DeleteMapping("/post/{postNum}")
    public ResponseEntity<ApiResponse<ResponseDTO>> removePost(@PathVariable("postNum") Long postNum){
        try{
            boardService.remove(postNum);
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                .body(ApiResponse.internalServerError(e.getMessage()));
        }
        return ResponseEntity.ok(ApiResponse.ok(new ResponseDTO(true)));
    }


    // 게시글 작성자 응답
    @GetMapping("/user/{postNum}")
    public ResponseEntity<ApiResponse<ResponseDTO>> checkPostUser(@PathVariable("postNum") Long postNum, HttpSession session){
        boolean result = false;
        try{
            result = boardService.checkUser(postNum, sessionService.getUserId(session));
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                .body(ApiResponse.internalServerError(e.getMessage()));
        }
        return ResponseEntity.ok(ApiResponse.ok(new BooleanDTO(true, result)));
    }
}
