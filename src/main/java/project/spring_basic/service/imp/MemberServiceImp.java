package project.spring_basic.service.imp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import project.spring_basic.data.dto.Request.MemberDTO;
import project.spring_basic.data.dto.Request.NewAccountDTO;
import project.spring_basic.data.dto.Response.ModelAttribute.AccountInfoDTO;
import project.spring_basic.data.entity.Member;
import project.spring_basic.service.MemberService;
import project.spring_basic.service.commands.MemberServiceCommands;
import project.spring_basic.service.querys.MemberServiceQuerys;

@Service
public class MemberServiceImp implements MemberService{
    
    @Autowired
    private MemberServiceCommands memberServiceCommands;

    @Autowired
    private MemberServiceQuerys memberServiceQuerys;
    

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
        return memberServiceQuerys.getAccountInfo(id);
    }



    // 회원 정보 DB 삽입
    // 동시에 여러 트랜잭션이 데이터를 삽입하는 것을 방지
    public void save(MemberDTO memberDTO) throws Exception {
        memberServiceCommands.save(memberDTO);
    }


    public void update(NewAccountDTO newAccountDTO, Long id) throws Exception{
        memberServiceCommands.update(newAccountDTO, id);
    }
}