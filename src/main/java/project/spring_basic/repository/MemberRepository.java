package project.spring_basic.repository;

import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.spring_basic.entity.Member;

import java.util.List;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    List<Member> findByUserId(String userId);
    List<Member> findByUserIdAndPassword(String userId, String password);
}
