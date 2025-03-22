package project.spring_basic.data.repository;

import org.springframework.stereotype.Repository;

import project.spring_basic.data.entity.Member;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    
    List<Member> findByUserId(String userId);

    boolean existsByUserId(String userId);

    boolean existsByUserIdAndPassword(String userId, String password);
}
