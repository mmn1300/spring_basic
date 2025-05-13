package project.spring_basic.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import project.spring_basic.data.entity.Member;
import project.spring_basic.data.repository.MemberRepository;


@ActiveProfiles("test")
@SpringBootTest
public class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;


    @Test
    @DisplayName("회원 데이터를 삽입하고 문자열 아이디를 기반으로 회원 정보를 조회한다.")
    public void memberSaveTest() throws Exception {

        ///////////
        // given //
        ///////////
        
        String userId = "tttttttt";
        String password = "tttttttt";
        String nickname = "테스트용 임시 계정";
        String email = "ttt@ttt.com";
        String phoneNumber = "000-0000-0000";
        // 나노초 이하 짤림 현상으로 인한 데이터 불일치 방지
        LocalDateTime now = LocalDateTime.now().withNano(0);
        Integer level = 1; 
        
        Member member = Member.builder()
            .userId(userId)
            .password(password)
            .nickname(nickname)
            .email(email)
            .phoneNumber(phoneNumber)
            .createAt(now)
            .level(level)
            .build();


        //////////
        // when //
        //////////
        
        // 실제 DB에 저장
        memberRepository.save(member);

        // 저장된 멤버를 조회하여 값 확인
        List<Member> retrievedMembers = memberRepository.findByUserId(userId);
        Member retrievedMember = retrievedMembers.get(0);


        //////////
        // then //
        //////////
        
        // 삽입된 데이터 개수 검증
        assertThat(retrievedMembers).hasSize(1);
        
        // AutoIncrement를 통해 부여된 id 값 검증
        assertNotNull(member.getId());  // id가 null이 아니어야 함
        assertEquals(1, retrievedMember.getId());

        // 삽입 데이터 값 검증
        assertThat(retrievedMembers)
            .extracting(Member::getUserId, Member::getPassword, Member::getNickname, Member::getEmail,
            Member::getPhoneNumber, Member::getCreateAt, Member::getLevel)
            .containsExactly(
            tuple(userId, password, nickname, email, phoneNumber, now, level)
            );

        
        // assertThat(retrievedMember)
        //     .extracting(Member::getUserId, Member::getPassword, Member::getNickname, Member::getEmail,
        //     Member::getPhoneNumber, Member::getCreateAt, Member::getLevel)
        //     .containsExactly(
        //     userId, password, nickname, email, phoneNumber, now, level
        //     );


        // assertEquals(userId, retrievedMember.getUserId());
        // assertEquals(password, retrievedMember.getPassword());
        // assertEquals(nickname, retrievedMember.getNickname());
        // assertEquals(email, retrievedMember.getEmail());
        // assertEquals(phoneNumber, retrievedMember.getPhoneNumber());
        // assertEquals(now, retrievedMember.getCreateAt()); 
        // assertEquals(level, retrievedMember.getLevel());
    }
}
