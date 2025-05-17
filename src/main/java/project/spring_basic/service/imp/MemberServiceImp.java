package project.spring_basic.service.imp;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import project.spring_basic.data.dao.MemberDAO;
import project.spring_basic.data.dto.Request.MemberDTO;
import project.spring_basic.data.dto.Request.NewAccountDTO;
import project.spring_basic.data.dto.Response.ModelAttribute.AccountInfoDTO;
import project.spring_basic.data.entity.Member;
import project.spring_basic.exception.DtoNullException;
import project.spring_basic.exception.MemberNotFoundException;
import project.spring_basic.service.MemberService;

@Service
public class MemberServiceImp implements MemberService{
    
    @Autowired
    private MemberDAO memberDAO;

    public MemberServiceImp(MemberDAO memberDAO) {
        this.memberDAO = memberDAO;
    }
    

    // 해당 ID를 가진 회원이 존재하는지 확인
    @Transactional(readOnly = true)
    public boolean memberExistsById (String userId) throws Exception {
        return memberDAO.existsByUserId(userId);
    }


    // 해당 ID와 비밀번호를 가진 회원이 존재하는지 확인
    @Transactional(readOnly = true)
    public boolean memberExists(String userId, String password) throws Exception {
        return memberDAO.existsByUserIdAndPassword(userId, password);
    }


    // 회원 정보 조회 id(문자열) - Member
    @Transactional(readOnly = true)
    public Member getMemberByUserId(String userId) throws Exception {
        List<Member> members = memberDAO.findByUserId(userId);
        if (members.isEmpty()) {
            throw new MemberNotFoundException("해당 회원은 존재하지 않습니다.");
        }
        return members.get(0);
    }


    // 회원 정보 조회 id(정수) - AccountInfoDTO
    @Transactional(readOnly = true)
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



    // 회원 정보 DB 삽입
    // 동시에 여러 트랜잭션이 데이터를 삽입하는 것을 방지
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void save(MemberDTO memberDTO) throws Exception {
        if(memberDTO == null){
            throw new DtoNullException("DTO에 값이 담겨있지 않습니다.");
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


    @Transactional
    public void update(NewAccountDTO newAccountDTO, Long id) throws Exception{
        if(newAccountDTO == null){
            throw new DtoNullException("DTO에 값이 담겨있지 않습니다.");
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