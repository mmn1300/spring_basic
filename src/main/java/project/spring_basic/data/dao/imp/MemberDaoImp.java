package project.spring_basic.data.dao.imp;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import project.spring_basic.data.dao.MemberDAO;

import project.spring_basic.data.entity.Member;
import project.spring_basic.data.repository.MemberRepository;


@Service
public class MemberDaoImp implements MemberDAO{

    @Autowired
    private MemberRepository memberRepository;


    public void save(Member member){
        memberRepository.save(member);
    }

    public List<Member> findByUserId(String userId){
        return memberRepository.findByUserId(userId);
    }

    public boolean existsByUserId(String userId){
        return memberRepository.existsByUserId(userId);
    }

    public boolean existsByUserIdAndPassword(String userId, String password){
        return memberRepository.existsByUserIdAndPassword(userId, password);
    }
}
