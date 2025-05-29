package project.spring_basic.service.querys.imp;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import project.spring_basic.data.dao.MemberDAO;
import project.spring_basic.data.dto.Response.ModelAttribute.AccountInfoDTO;
import project.spring_basic.data.entity.Member;
import project.spring_basic.exception.MemberNotFoundException;
import project.spring_basic.service.querys.MemberServiceQuerys;

@Service
@Transactional(readOnly = true)
public class MemberServiceQuerysImp implements MemberServiceQuerys {
        
    @Autowired
    private MemberDAO memberDAO;

    public MemberServiceQuerysImp(MemberDAO memberDAO) {
        this.memberDAO = memberDAO;
    }


    public Member getMember(Long id) throws Exception{
        return memberDAO.findById(id).map(m -> m)
                .orElseThrow(() -> new MemberNotFoundException("해당 회원은 존재하지 않습니다."));
    }
    

    // 회원 정보 조회 id(문자열) - Member
    public Member getMemberByUserId(String userId) throws Exception {
        List<Member> members = memberDAO.findByUserId(userId);
        if (members.isEmpty()) {
            throw new MemberNotFoundException("해당 회원은 존재하지 않습니다.");
        }
        return members.get(0);
    }
    

    // 해당 ID를 가진 회원이 존재하는지 확인
    public boolean memberExistsById (String userId) throws Exception {
        return memberDAO.existsByUserId(userId);
    }


    // 해당 ID와 비밀번호를 가진 회원이 존재하는지 확인
    public boolean memberExists(String userId, String password) throws Exception {
        return memberDAO.existsByUserIdAndPassword(userId, password);
    }

    // 회원 정보 조회 id(정수) - AccountInfoDTO
    public AccountInfoDTO getAccountInfo(Long id) throws Exception {
        AccountInfoDTO accountInfoDTO = new AccountInfoDTO(null, "", "", "", "");
        if(id <= 0L){
            throw new IllegalArgumentException("양의 정수를 입력해야 합니다.");
        }else{
            Member member = memberDAO.findById(id).map(m -> m)
                .orElseThrow(() -> new MemberNotFoundException("해당 회원은 존재하지 않습니다."));
            accountInfoDTO.setId(id);
            accountInfoDTO.setUserId(member.getUserId());
            accountInfoDTO.setNickname(member.getNickname());
            accountInfoDTO.setEmail(member.getEmail());
            accountInfoDTO.setPhone(member.getPhoneNumber());
        }
        return accountInfoDTO;
    }
}
