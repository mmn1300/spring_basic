package project.spring_basic.data.dao;

import java.util.List;

import project.spring_basic.data.entity.Member;

public interface MemberDAO {

    public void save(Member member);

    public List<Member> findByUserId(String userId);

    public boolean existsByUserId(String userId);

    public boolean existsByUserIdAndPassword(String userId, String password);
}
