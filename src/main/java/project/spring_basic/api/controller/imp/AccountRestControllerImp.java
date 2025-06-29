package project.spring_basic.api.controller.imp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import project.spring_basic.api.ApiResponse;
import project.spring_basic.api.controller.AccountRestController;
import project.spring_basic.data.dto.Request.AccountDTO;
import project.spring_basic.data.dto.Request.MemberDTO;
import project.spring_basic.data.dto.Request.NewAccountDTO;
import project.spring_basic.data.dto.Response.Json.BooleanDTO;
import project.spring_basic.data.dto.Response.Json.ResponseDTO;
import project.spring_basic.service.MemberService;
import project.spring_basic.service.SessionService;

@RestController
@RequestMapping("/account")
public class AccountRestControllerImp implements AccountRestController {
    
    @Autowired
    private MemberService memberService;

    @Autowired
    private SessionService sessionService;


    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ResponseDTO>> checkId(@PathVariable("id") String userId) throws Exception {
        BooleanDTO data = new BooleanDTO(true, memberService.memberExistsById(userId));
        return ResponseEntity.ok(ApiResponse.ok(data));
    }



    @PostMapping("/check")
    public ResponseEntity<ApiResponse<ResponseDTO>> checkAccount(@Valid @RequestBody AccountDTO accountDTO) throws Exception {
        String id = accountDTO.getId();
        String pw = accountDTO.getPw();
        BooleanDTO data = new BooleanDTO(true, memberService.memberExists(id, pw));
        return ResponseEntity.ok(ApiResponse.ok(data));
    }



    @PostMapping("/member")
    public ResponseEntity<ApiResponse<ResponseDTO>> setMember(@Valid @RequestBody MemberDTO memberDTO) throws Exception {
        memberService.save(memberDTO);
        ResponseDTO data = new ResponseDTO(true);
        return ResponseEntity.ok(ApiResponse.ok(data));
    }



    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ResponseDTO>> updateAccount(@Valid @RequestBody NewAccountDTO newAccountDTO, HttpSession session) throws Exception {
        Long sessionUserId = sessionService.getId(session);
        
        // DB 계정 정보 수정 로직
        memberService.update(newAccountDTO, sessionUserId);
        // 세션 정보 갱신
        sessionService.updateSession(session, newAccountDTO);
        ResponseDTO data = new ResponseDTO(true);
        return ResponseEntity.ok(ApiResponse.ok(data));
    }

}
