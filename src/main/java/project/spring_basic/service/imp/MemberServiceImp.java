package project.spring_basic.service.imp;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import project.spring_basic.data.dao.MemberDAO;
import project.spring_basic.data.dto.Request.MemberDTO;
import project.spring_basic.data.dto.Request.NewAccountDTO;
import project.spring_basic.data.dto.Response.ModelAttribute.AccountInfoDTO;
import project.spring_basic.data.entity.Member;
import project.spring_basic.service.MemberService;

@Service
public class MemberServiceImp implements MemberService{
    
    @Autowired
    private MemberDAO memberDAO;

    public MemberServiceImp(MemberDAO memberDAO) {
        this.memberDAO = memberDAO;
    }
    

    // 해당 ID를 가진 회원이 존재하는지 확인
    public boolean memberExistsById (String userId) throws Exception {
        return memberDAO.existsByUserId(userId);
    }

    // 해당 ID와 비밀번호를 가진 회원이 존재하는지 확인
    public boolean memberExists(String userId, String password) throws Exception {
        return memberDAO.existsByUserIdAndPassword(userId, password);
    }


    // 회원 정보 DB 삽입
    // 동시에 여러 트랜잭션이 데이터를 삽입하는 것을 방지
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void save(MemberDTO memberDTO) throws Exception {
        Member member = new Member();

        member.setUserId(memberDTO.getUserId());
        member.setPassword(memberDTO.getPw());
        member.setNickname(memberDTO.getName());
        member.setPhoneNumber(memberDTO.getPhone());
        member.setEmail(memberDTO.getEmail());
        member.setCreateAt(LocalDateTime.now());
        member.setLevel(1);

        memberDAO.save(member);
    }


    public Member getMemberInfo(String userId) throws Exception {
        return memberDAO.findByUserId(userId).get(0);
    }


    public AccountInfoDTO getAccountInfo(Long id) throws Exception {
        AccountInfoDTO accountInfoDTO = new AccountInfoDTO("", "", "", "");
        if(id > 0L){
            Member member = memberDAO.findById(id).get();    
            accountInfoDTO.setUserId(member.getUserId());
            accountInfoDTO.setNickname(member.getNickname());
            accountInfoDTO.setEmail(member.getEmail());
            accountInfoDTO.setPhone(member.getPhoneNumber());
        }
        return accountInfoDTO;
    }


    public void update(NewAccountDTO newAccountDTO, Long id) throws Exception{
        if(id > 0L){
            Member member = memberDAO.findById(id).get();
            member.setUserId(newAccountDTO.getUserId());
            member.setNickname(newAccountDTO.getNickname());
            member.setEmail(newAccountDTO.getEmail());
            member.setPhoneNumber(newAccountDTO.getPhone());
            memberDAO.save(member);
        }
    }
}