package project.spring_basic.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import jakarta.transaction.Transactional;
import project.spring_basic.data.entity.Member;
import project.spring_basic.data.repository.MemberRepository;

@SpringBootTest
public class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;


    @Test
    @Transactional
    public void memberSaveTest() throws Exception {
        Member member = new Member(
            null,
            "tttttttt",
            "tttttttt",
            "테스트용 임시 계정",
            "ttt@ttt.com",
            "000-0000-0000",
            LocalDateTime.now(),
            1
        );

        // 실제 DB에 저장
        memberRepository.save(member);

        // 저장된 멤버를 조회하여 값 확인
        Member retrievedMember = memberRepository.findByUserId("tttttttt").get(0);

        assertNotNull(member.getId());  // id가 null이 아니어야 함
        System.out.println("\n[ " + member.getId() + " ]\n");
        assertEquals("tttttttt", retrievedMember.getUserId());
        assertEquals("tttttttt", retrievedMember.getPassword());
        assertEquals("테스트용 임시 계정", retrievedMember.getNickname());
        assertEquals("ttt@ttt.com", retrievedMember.getEmail());
        assertEquals("000-0000-0000", retrievedMember.getPhoneNumber());

        // 삽입 후에는 롤백해도 AUTO_INCREMENT 값이 증가한 상태니 주의할 것
    }
}
