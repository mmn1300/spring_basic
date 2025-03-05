// package project.spring_basic.service;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.stereotype.Service;

// import project.spring_basic.repository.MemberRepository;

// @Service
// public class MemberService {
    
//     @Autowired
//     private MemberRepository memberRepository;

//     // 해당 ID를 가진 회원이 존재하는지 확인
//     public boolean memberExistsById (String userId) throws Exception{
//         return memberRepository.findByUserId(userId).isPresent();
//     }
// }