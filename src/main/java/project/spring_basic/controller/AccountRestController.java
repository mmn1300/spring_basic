// package project.spring_basic.controller;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.web.bind.annotation.PathVariable;
// import org.springframework.web.bind.annotation.PostMapping;
// import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.bind.annotation.RestController;

// import project.spring_basic.dto.ResponseDTO;
// import project.spring_basic.service.MemberService;

// @RestController
// @RequestMapping("/account")
// public class AccountRestController {

//     @Autowired
//     private MemberService memberService;

//     // 아이디 검사
//     @PostMapping("/{id}")
//     public ResponseDTO checkId(@PathVariable("id") String userId) throws Exception {
//         ResponseDTO response = new ResponseDTO();
//         response.setMessage(memberService.memberExistsById(userId));
//         return response;
//     }

//     // 아이디 비밀번호 일치 검사
//     // 로그인
//     // 로그아웃
//     // 계정 생성
// }
