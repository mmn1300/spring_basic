package project.spring_basic.service.commands.imp;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import project.spring_basic.data.dao.MemberDAO;
import project.spring_basic.data.dto.Request.MemberDTO;
import project.spring_basic.data.dto.Request.NewAccountDTO;
import project.spring_basic.data.entity.Member;
import project.spring_basic.exception.DtoNullException;
import project.spring_basic.exception.MemberNotFoundException;

import project.spring_basic.service.commands.MemberServiceCommands;


@Service
@Transactional
public class MemberServiceCommandsImp implements MemberServiceCommands {
        
    @Autowired
    private MemberDAO memberDAO;

    public MemberServiceCommandsImp(MemberDAO memberDAO) {
        this.memberDAO = memberDAO;
    }


    
    // 회원 정보 DB 삽입
    // 동시에 여러 트랜잭션이 데이터를 삽입하는 것을 방지
    @Transactional(isolation = Isolation.SERIALIZABLE)
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

        memberDAO.save(member);
    }


    public void update(NewAccountDTO newAccountDTO, Long id) throws Exception{
        if(newAccountDTO == null){
            throw new DtoNullException("DTO가 존재하지 않습니다.");
        }
        if(id <= 0L){
            throw new IllegalArgumentException("양의 정수를 입력해야 합니다.");
        }else{
            Member member = memberDAO.findById(id).map(m -> m)
                .orElseThrow(() -> new MemberNotFoundException("해당 회원은 존재하지 않습니다."));;
            member.setUserId(newAccountDTO.getUserId());
            member.setNickname(newAccountDTO.getNickname());
            member.setEmail(newAccountDTO.getEmail());
            member.setPhoneNumber(newAccountDTO.getPhone());
            memberDAO.save(member);
        }
    }
}
