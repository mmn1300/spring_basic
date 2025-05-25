package project.spring_basic.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.RequestBody;

import project.spring_basic.api.ApiResponse;
import project.spring_basic.data.dto.Request.AccountDTO;
import project.spring_basic.data.dto.Request.MemberDTO;
import project.spring_basic.data.dto.Request.NewAccountDTO;
import project.spring_basic.data.dto.Response.Json.BooleanDTO;
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
    public ResponseEntity<ApiResponse<ResponseDTO>> checkId(@PathVariable("id") String userId) throws Exception {
        BooleanDTO data = new BooleanDTO(true, memberService.memberExistsById(userId));
        return ResponseEntity.ok(ApiResponse.ok(data));
    }


    // 아이디 비밀번호 일치 검사
    @PostMapping("/check")
    public ResponseEntity<ApiResponse<ResponseDTO>> checkAccount(@Valid @RequestBody AccountDTO accountDTO) throws Exception {
        String id = accountDTO.getId();
        String pw = accountDTO.getPw();
        BooleanDTO data = new BooleanDTO(true, memberService.memberExists(id, pw));
        return ResponseEntity.ok(ApiResponse.ok(data));
    }


    // 계정 생성
    @PostMapping("/member")
    public ResponseEntity<ApiResponse<ResponseDTO>> setMember(@Valid @RequestBody MemberDTO memberDTO) throws Exception {
        memberService.save(memberDTO);
        ResponseDTO data = new ResponseDTO(true);
        return ResponseEntity.ok(ApiResponse.ok(data));
    }


    // 계정 정보 수정
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ResponseDTO>> updateAccount(@Valid @RequestBody NewAccountDTO newAccountDTO, HttpSession session) throws Exception {
        // DB 계정 정보 수정 로직
        memberService.update(newAccountDTO, sessionService.getId(session));
        // 세션 정보 갱신
        sessionService.updateSession(session, newAccountDTO);
        ResponseDTO data = new ResponseDTO(true);
        return ResponseEntity.ok(ApiResponse.ok(data));
    }
}
