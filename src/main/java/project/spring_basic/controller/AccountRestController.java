package project.spring_basic.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.bind.annotation.RequestBody;

import jakarta.servlet.http.HttpSession;
import project.spring_basic.dto.Request.AccountDTO;
import project.spring_basic.dto.Request.MemberDTO;
import project.spring_basic.dto.Response.ErrorDTO;
import project.spring_basic.dto.Response.ResponseDTO;
import project.spring_basic.dto.Response.UserInfoDTO;
import project.spring_basic.service.MemberService;
import project.spring_basic.service.SessionService;


@RestController
@RequestMapping("/account")
public class AccountRestController {

    @Autowired
    private MemberService memberService;

    @Autowired
    private SessionService sessionService;

    // 아이디 검사
    @GetMapping("/{id}")
    public ResponseDTO checkId(@PathVariable("id") String userId) throws Exception {
        try{
            return new ResponseDTO(memberService.memberExistsById(userId));
        }catch(Exception e){
            return new ErrorDTO(false, e.getMessage());
        }
    }

    // 아이디 비밀번호 일치 검사
    @PostMapping("/check")
    public ResponseDTO checkAccount(@RequestBody AccountDTO accountDTO) {
        try{
            String id = accountDTO.getId();
            String pw = accountDTO.getPw();
            return new ResponseDTO(memberService.memberExists(id, pw));
        }catch(Exception e){
            return new ErrorDTO(false, e.getMessage());
        }
    }

    // 로그아웃
    @DeleteMapping("/logout")
    public ResponseDTO logout(HttpSession session, RedirectAttributes redirectAttributes) {
        try{
            sessionService.deleteAllSession(session);
        }catch(Exception e){
            return new ErrorDTO(false, e.getMessage());
        }
        return new ResponseDTO(true);
    }


    // 계정 생성
    @PostMapping("/member")
    public ResponseDTO setMember(@RequestBody MemberDTO memberDTO) {
        try{
            memberService.save(memberDTO);
            return new ResponseDTO(true);
        }catch(Exception e){
            return new ErrorDTO(false, e.getMessage());
        }
    }
    
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
    
}
