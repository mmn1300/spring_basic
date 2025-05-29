package project.spring_basic.service.querys.imp;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import project.spring_basic.data.dao.MemberDAO;
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


    /* 
    * 
    * 테이블 작업 중 발생하는 예외에 대한 처리,
    * 테이블에 대한 직접적인 작업을 수행하는 2차 서비스 처리 계층
    * 
    */
    


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
}
