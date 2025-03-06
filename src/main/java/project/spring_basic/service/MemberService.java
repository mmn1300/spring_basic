package project.spring_basic.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import project.spring_basic.dto.Request.MemberDTO;
import project.spring_basic.entity.Member;
import project.spring_basic.repository.MemberRepository;

@Service
public class MemberService {
    
    @Autowired
    private MemberRepository memberRepository;

    // 해당 ID를 가진 회원이 존재하는지 확인
    public boolean memberExistsById (String userId) throws Exception{
        return !memberRepository.findByUserId(userId).isEmpty();
    }


    // 회원 정보 DB 삽입
    public void save(MemberDTO memberDTO) throws Exception {
        Member member = new Member();

        member.setUserId(memberDTO.getUserId());
        member.setPassword(memberDTO.getPw());
        member.setNickname(memberDTO.getName());
        member.setPhoneNumber(memberDTO.getPhone());
        member.setEmail(memberDTO.getEmail());
        member.setCreateAt(LocalDateTime.now());
        member.setLevel(1);

        memberRepository.save(member);
    }
}