package project.spring_basic.service.imp;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import project.spring_basic.data.dto.Request.MemberDTO;
import project.spring_basic.data.dto.Request.NewAccountDTO;
import project.spring_basic.data.dto.Response.ModelAttribute.AccountInfoDTO;
import project.spring_basic.data.entity.Member;
import project.spring_basic.exception.DtoNullException;
import project.spring_basic.service.MemberService;
import project.spring_basic.service.commands.MemberServiceCommands;
import project.spring_basic.service.querys.MemberServiceQuerys;

@Service
public class MemberServiceImp implements MemberService{
    
    @Autowired
    private MemberServiceCommands memberServiceCommands;

    @Autowired
    private MemberServiceQuerys memberServiceQuerys;


    /* 
    * 
    * 입력값에 대한 예외,
    * 책임 분리를 위한 1차 서비스 처리 계층
    * 
    */
    

    // 해당 ID를 가진 회원이 존재하는지 확인
    public boolean memberExistsById (String userId) throws Exception {
        return memberServiceQuerys.memberExistsById(userId);
    }


    // 해당 ID와 비밀번호를 가진 회원이 존재하는지 확인
    @Transactional(readOnly = true)
    public boolean memberExists(String userId, String password) throws Exception {
        return memberServiceQuerys.memberExists(userId, password);
    }


    // 회원 정보 조회 id(문자열) - Member
    public Member getMemberByUserId(String userId) throws Exception {
        return memberServiceQuerys.getMemberByUserId(userId);
    }


    // 회원 정보 조회 id(정수) - AccountInfoDTO
    public AccountInfoDTO getAccountInfo(Long id) throws Exception {
        if(id <= 0L){
            throw new IllegalArgumentException("양의 정수를 입력해야 합니다.");
        }
        Member member = memberServiceQuerys.getMember(id);

        AccountInfoDTO accountInfoDTO = new AccountInfoDTO(
            member.getId(),
            member.getUserId(),
            member.getNickname(),
            member.getEmail(),
            member.getPhoneNumber()
        );
        return accountInfoDTO;
    }



    // 회원 정보 DB 삽입
    // 동시에 여러 트랜잭션이 데이터를 삽입하는 것을 방지
    public void save(MemberDTO memberDTO) throws Exception {
        if(memberDTO == null){
            throw new DtoNullException("DTO가 존재하지 않습니다.");
        }

        Member member = Member.builder()
            .userId(memberDTO.getUserId())
            .password(memberDTO.getPw())
            .nickname(memberDTO.getName())
            .email(memberDTO.getEmail())
            .phoneNumber(memberDTO.getPhone())
            .createAt(LocalDateTime.now())
            .level(1)
            .build();

        memberServiceCommands.save(member);
    }


    public void update(NewAccountDTO newAccountDTO, Long id) throws Exception {
        if(newAccountDTO == null){
            throw new DtoNullException("DTO가 존재하지 않습니다.");
        }
        if(id <= 0L){
            throw new IllegalArgumentException("양의 정수를 입력해야 합니다.");
        }
        Member member = memberServiceQuerys.getMember(id);
        
        memberServiceCommands.update(newAccountDTO, member);
    }
}