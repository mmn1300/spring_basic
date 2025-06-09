package project.spring_basic.service.commands.imp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import project.spring_basic.data.dao.MemberDAO;
import project.spring_basic.data.dto.Request.NewAccountDTO;
import project.spring_basic.data.entity.Member;

import project.spring_basic.service.commands.MemberServiceCommands;


@Service
@Transactional
public class MemberServiceCommandsImp implements MemberServiceCommands {
        
    @Autowired
    private MemberDAO memberDAO;

    public MemberServiceCommandsImp(MemberDAO memberDAO) {
        this.memberDAO = memberDAO;
    }



    /* 
    * 
    * 테이블 작업 중 발생하는 예외에 대한 처리,
    * 테이블에 대한 직접적인 작업을 수행하는 2차 서비스 처리 계층
    * 
    */


    
    // 회원 정보 DB 삽입
    // 동시에 여러 트랜잭션이 데이터를 삽입하는 것을 방지
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void save(Member member) throws Exception {
        memberDAO.save(member);
    }


    public void update(NewAccountDTO newAccountDTO, Member member) throws Exception{
        member.setUserId(newAccountDTO.getUserId());
        member.setNickname(newAccountDTO.getNickname());
        member.setEmail(newAccountDTO.getEmail());
        member.setPhoneNumber(newAccountDTO.getPhone());
    }
}
