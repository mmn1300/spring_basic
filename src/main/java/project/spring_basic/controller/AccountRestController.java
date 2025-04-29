package project.spring_basic.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.RequestBody;

import project.spring_basic.data.dto.Request.AccountDTO;
import project.spring_basic.data.dto.Request.MemberDTO;
import project.spring_basic.data.dto.Request.NewAccountDTO;
import project.spring_basic.data.dto.Response.Json.ErrorDTO;
import project.spring_basic.data.dto.Response.Json.ResponseDTO;
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
    public ResponseDTO checkAccount(@Valid @RequestBody AccountDTO accountDTO) {
        try{
            String id = accountDTO.getId();
            String pw = accountDTO.getPw();
            return new ResponseDTO(memberService.memberExists(id, pw));
        }catch(Exception e){
            return new ErrorDTO(false, e.getMessage());
        }
    }


    // 계정 생성
    @PostMapping("/member")
    public ResponseDTO setMember(@Valid @RequestBody MemberDTO memberDTO) {
        try{
            memberService.save(memberDTO);
            return new ResponseDTO(true);
        }catch(Exception e){
            return new ErrorDTO(false, e.getMessage());
        }
    }

    // 계정 정보 수정
    @PutMapping("/{id}")
    public ResponseDTO updateAccount(@Valid @RequestBody NewAccountDTO newAccountDTO, HttpSession session){
        try{
            // DB 계정 정보 수정 로직
            memberService.update(newAccountDTO, sessionService.getId(session));
            // 세션 정보 갱신
            sessionService.updateSession(session, newAccountDTO);
            return new ResponseDTO(true);
        }catch(Exception e){
            return new ErrorDTO(false, e.getMessage());
        }
    }
}
