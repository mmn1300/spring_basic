package project.spring_basic.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpSession;
import project.spring_basic.api.ApiResponse;
import project.spring_basic.data.dto.Response.Json.ResponseDTO;
import project.spring_basic.data.dto.Response.Json.UserInfoDTO;
import project.spring_basic.service.SessionService;


@RestController
@RequestMapping("/session")
public class SessionRestController {

    @Autowired
    private SessionService sessionService;

    // 세션 정보 조회
    @GetMapping
    public ResponseEntity<ApiResponse<ResponseDTO>> sessionInfo(HttpSession session) throws Exception {
        UserInfoDTO dto = new UserInfoDTO(false, null, null);
        dto = sessionService.getUserInfo(dto, session);
        return ResponseEntity.ok(ApiResponse.ok(dto));
    }


    // 로그아웃
    @DeleteMapping
    public ResponseEntity<ApiResponse<ResponseDTO>> logout(HttpSession session) throws Exception {
        sessionService.deleteAllSession(session);
        return ResponseEntity.ok(ApiResponse.ok(new ResponseDTO(true)));
    }
}
