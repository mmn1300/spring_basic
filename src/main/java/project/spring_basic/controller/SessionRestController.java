package project.spring_basic.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpSession;
import project.spring_basic.dto.Response.ErrorDTO;
import project.spring_basic.dto.Response.ResponseDTO;
import project.spring_basic.dto.Response.UserInfoDTO;
import project.spring_basic.service.SessionService;


@RestController
public class SessionRestController {

    @Autowired
    private SessionService sessionService;

    // 세션 정보 조회
    @GetMapping("/session")
    public ResponseDTO sessionInfo(HttpSession session) {
        UserInfoDTO dto = new UserInfoDTO(false, null, null);
        try{
            dto = sessionService.getUserInfo(dto, session);
        }catch(Exception e){
            return new ErrorDTO(false, e.getMessage());
        }
        return dto;
    }


    // 로그아웃
    @DeleteMapping("/session/logout")
    public ResponseDTO logout(HttpSession session, RedirectAttributes redirectAttributes) {
        try{
            sessionService.deleteAllSession(session);
        }catch(Exception e){
            return new ErrorDTO(false, e.getMessage());
        }
        return new ResponseDTO(true);
    }
}
