package project.spring_basic.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpSession;
import project.spring_basic.api.ApiResponse;
import project.spring_basic.data.dto.Response.Json.ResponseDTO;
import project.spring_basic.data.dto.Response.Json.UserInfoDTO;
import project.spring_basic.service.SessionService;


@RestController
public class SessionRestController {

    @Autowired
    private SessionService sessionService;

    // 세션 정보 조회
    @GetMapping("/session")
    public ResponseEntity<ApiResponse<ResponseDTO>> sessionInfo(HttpSession session) {
        UserInfoDTO dto = new UserInfoDTO(false, null, null);
        try{
            dto = sessionService.getUserInfo(dto, session);
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                .body(ApiResponse.internalServerError(e.getMessage()));
        }
        return ResponseEntity.ok(ApiResponse.ok(dto));
    }


    // 로그아웃
    @DeleteMapping("/session/logout")
    public ResponseEntity<ApiResponse<ResponseDTO>> logout(HttpSession session) {
        try{
            sessionService.deleteAllSession(session);
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                .body(ApiResponse.internalServerError(e.getMessage()));
        }
        return ResponseEntity.ok(ApiResponse.ok(new ResponseDTO(true)));
    }
}
