package project.spring_basic.service.MemberServiceTest.integration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.PlatformTransactionManager;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import project.spring_basic.data.repository.MemberRepository;
import project.spring_basic.service.MemberService;

@ActiveProfiles("test")
@SpringBootTest
public abstract class MemberServiceIntegrationTestSupport {
            
    @Autowired MemberService memberService;

    @Autowired MemberRepository memberRepository;

    @PersistenceContext EntityManager entityManager;

    @Autowired PlatformTransactionManager transactionManager;
}
