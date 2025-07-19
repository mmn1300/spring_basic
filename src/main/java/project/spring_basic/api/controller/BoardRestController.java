package project.spring_basic.api.controller;

// import org.slf4j.Logger;
// import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

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
import project.spring_basic.api.swaggerDto.SwaggerBooleanDTO;
import project.spring_basic.api.swaggerDto.SwaggerErrorDTO;
import project.spring_basic.api.swaggerDto.SwaggerFileNameDTO;
import project.spring_basic.api.swaggerDto.SwaggerNullDTO;
import project.spring_basic.api.swaggerDto.SwaggerNumberDTO;
import project.spring_basic.api.swaggerDto.SwaggerPostsDTO;
import project.spring_basic.api.swaggerDto.SwaggerResponseDTO;
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

    @Operation(
        summary = "게시글 목록 조회",
        description = "page 번호만 포함된 요청으로 게시글 목록을 조회함.",
        parameters = {
            @Parameter(
                name = "page",
                description = "조회할 페이지 번호",
                required = true,
                schema = @Schema(type = "integer", minimum = "1")
            )
        },
        responses = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "200",
                description = "게시글 목록 조회 성공",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = SwaggerPostsDTO.class)
                )
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "500",
                description = "서버 오류로 인한 실패",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = SwaggerErrorDTO.class)
                )
            ),
        }
    )
    @GetMapping(value = "/posts", params = {"page", "!user"})
    public ResponseEntity<ApiResponse<ResponseDTO>> getPosts(HttpServletRequest request) throws Exception{
        int pageNum = Integer.parseInt(request.getParameter("page"));
        return ResponseEntity.ok(ApiResponse.ok(boardService.getPostsInfo(pageNum)));
    }



    @Operation(
        summary = "게시글 목록 조회(유저 별)",
        description = "page 번호에 해당하는 해당 사용자가 작성한 게시글 목록을 조회함.",
        parameters = {
            @Parameter(
                name = "page",
                description = "조회할 페이지 번호",
                required = true,
                schema = @Schema(type = "integer", minimum = "1")
            ),
            @Parameter(
                name = "user",
                description = "게시글 작성자의 정수 id 값",
                required = true,
                schema = @Schema(type = "integer", minimum = "1")
            )
        },
        responses = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "200",
                description = "게시글 목록 조회 성공",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = SwaggerPostsDTO.class)
                )
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "500",
                description = "서버 오류로 인한 실패",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = SwaggerErrorDTO.class)
                )
            ),
        }
    )
    @GetMapping(value = "/posts", params = {"page", "user"})
    public ResponseEntity<ApiResponse<ResponseDTO>> getPostsByUser(HttpServletRequest request) throws Exception{
        int pageNum = Integer.parseInt(request.getParameter("page"));
        Long userId = Long.parseLong(request.getParameter("user"));
        return ResponseEntity.ok(ApiResponse.ok(boardService.getPostsInfoByUser(pageNum, userId)));
    }



    @Operation(
        summary = "내 작성글 수",
        description = "지정한 사용자가 작성한 총 게시글의 수를 응답함.",
        parameters = {
            @Parameter(
                name = "userId",
                description = "사용자의 문자열 id 값",
                required = true
            )
        },
        responses = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "200",
                description = "작성된 게시글 수 조회 성공",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = SwaggerNumberDTO.class)
                )
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "500",
                description = "서버 오류로 인한 실패",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = SwaggerErrorDTO.class)
                )
            ),
        }
    )
    @GetMapping("/{userId}/posts/count")
    public ResponseEntity<ApiResponse<ResponseDTO>> getCount(@PathVariable("userId") String userId) throws Exception {
        int number = boardService.getUserPostCount(userId);
        return ResponseEntity.ok(ApiResponse.ok(new NumberDTO(true, number)));
    }
    

    
    @Operation(
        summary = "게시글 생성",
        description = "작성 내용을 기반으로 데이터베이스내에 게시글을 생성한다.",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            required = true,
            description = "작성할 게시글 정보 (제목, 내용, 파일)",
            content = @Content(
                mediaType = "multipart/form-data",
                schema = @Schema(implementation = PostDTO.class)
            )
        ),
        responses = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "200",
                description = "게시글 생성 성공",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = SwaggerResponseDTO.class)
                )
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "400",
                description = "요청 데이터 형식 미준수",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = SwaggerNullDTO.class)
                )
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "401",
                description = "인증 정보 미존재로 인한 접근 차단",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = SwaggerErrorDTO.class)
                )
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "403",
                description = "CSRF 토큰 미존재로 인한 요청 처리 거부",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = SwaggerErrorDTO.class)
                )
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "500",
                description = "서버 오류로 인한 실패",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = SwaggerErrorDTO.class)
                )
            ),
        }
    )
    @PostMapping("/post")
    public ResponseEntity<ApiResponse<ResponseDTO>> storePost(@ModelAttribute @Valid PostDTO postDTO,
                             @RequestParam(value="file", required=false) MultipartFile file,
                             HttpSession session) throws Exception {
        boardService.save(postDTO, sessionService.getId(session), file);
        return ResponseEntity.ok(ApiResponse.ok(new ResponseDTO(true)));
    }
    

    
    @Operation(
        summary = "첨부 파일 조회",
        description = "해당 게시글에 첨부되어있는 파일을 조회한다. 파일 존재시 파일명을 응답한다.",
        parameters = {
            @Parameter(
                name = "postNum",
                description = "조회할 게시글 번호",
                required = true,
                schema = @Schema(type = "integer", minimum = "1")
            )
        },
        responses = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "200",
                description = "첨부파일 존재 여부 조회 성공",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = SwaggerFileNameDTO.class)
                )
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "500",
                description = "서버 오류로 인한 실패",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = SwaggerErrorDTO.class)
                )
            ),
        }
    )
    @GetMapping("/file/{postNum}")
    public ResponseEntity<ApiResponse<ResponseDTO>> getFileName(@PathVariable("postNum") Long postNum)  throws Exception {
        String result = boardService.isFileExists(postNum);
        return ResponseEntity.ok(ApiResponse.ok(new FileNameDTO(true, result)));
    }
    

    @Operation(
        summary = "파일 다운로드",
        description = "게시글 번호에 해당하는 파일을 다운로드함",
        parameters = {
            @Parameter(
                name = "postNum",
                description = "게시글 번호",
                required = true
            )
        },
        responses = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "200",
                description = "첨부파일 다운로드 성공",
                content = @Content(
                    mediaType = "application/octet-stream"
                )
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "404",
                description = "다운로드할 파일 찾기 실패",
                content = @Content(
                    mediaType = "text/plain",
                    schema = @Schema(example = "File not found")
                )
            ),

        }
    )
    @GetMapping("/download/{postNum}")
    public ResponseEntity<?> fileDownload(@PathVariable("postNum") Long postNum) throws Exception {
        return boardService.getFile(postNum);
    }


    @Operation(
        summary = "게시글 수정",
        description = "해당 게시글을 수정하여 저장한다",
        parameters = {
            @Parameter(
                name = "postNum",
                description = "게시글 번호",
                required = true,
                schema = @Schema(type = "integer", minimum = "1")
            )
        },
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            required = true,
            description = "수정할 게시글 정보 (제목, 내용, 파일)",
            content = @Content(
                mediaType = "multipart/form-data",
                schema = @Schema(implementation = PostDTO.class)
            )
        ),
        responses = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "200",
                description = "게시글 수정 성공",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = SwaggerResponseDTO.class)
                )
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "400",
                description = "요청 데이터 형식 미준수",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = SwaggerNullDTO.class)
                )
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "401",
                description = "인증 정보 미존재로 인한 접근 차단",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = SwaggerErrorDTO.class)
                )
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "403",
                description = "CSRF 토큰 미존재로 인한 요청 처리 거부",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = SwaggerErrorDTO.class)
                )
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "500",
                description = "서버 오류로 인한 실패",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = SwaggerErrorDTO.class)
                )
            ),
        }
    )
    @PutMapping("/post/{postNum}")
    public ResponseEntity<ApiResponse<ResponseDTO>> updatePost(@PathVariable("postNum") Long postNum, @Valid PostDTO postDTO,
                              @RequestParam(value="file", required=false) MultipartFile file) throws Exception {
        boardService.update(postNum, postDTO, file);
        return ResponseEntity.ok(ApiResponse.ok(new ResponseDTO(true)));
    }

    
    
    @Operation(
        summary = "게시글 삭제",
        description = "해당 게시글을 삭제한다",
        parameters = {
            @Parameter(
                name = "postNum",
                description = "게시글 번호",
                required = true,
                schema = @Schema(type = "integer", minimum = "1")
            )
        },
        responses = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "200",
                description = "게시글 삭제 성공",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = SwaggerResponseDTO.class)
                )
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "401",
                description = "인증 정보 미존재로 인한 접근 차단",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = SwaggerErrorDTO.class)
                )
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "403",
                description = "CSRF 토큰 미존재로 인한 요청 처리 거부",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = SwaggerErrorDTO.class)
                )
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "500",
                description = "서버 오류로 인한 실패",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = SwaggerErrorDTO.class)
                )
            ),
        }
    )
    @DeleteMapping("/post/{postNum}")
    public ResponseEntity<ApiResponse<ResponseDTO>> removePost(@PathVariable("postNum") Long postNum) throws Exception {
        boardService.remove(postNum);
        return ResponseEntity.ok(ApiResponse.ok(new ResponseDTO(true)));
    }


    
    @Operation(
        summary = "작성자 확인",
        description = "게시글에 대한 작성자가 현재 접속중인 사용자인지를 응답한다.",
        parameters = {
            @Parameter(
                name = "postNum",
                description = "게시글의 정수 id 값",
                required = true
            )
        },
        responses = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "200",
                description = "작성자 일치 여부 조회 성공",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = SwaggerBooleanDTO.class)
                )
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "500",
                description = "서버 오류로 인한 실패",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = SwaggerErrorDTO.class)
                )
            ),
        }
    )
    @GetMapping("/user/{postNum}")
    public ResponseEntity<ApiResponse<ResponseDTO>> checkPostUser(@PathVariable("postNum") Long postNum, HttpSession session) throws Exception {
        boolean result = boardService.checkUser(postNum, sessionService.getUserId(session));
        return ResponseEntity.ok(ApiResponse.ok(new BooleanDTO(true, result)));
    }
}
