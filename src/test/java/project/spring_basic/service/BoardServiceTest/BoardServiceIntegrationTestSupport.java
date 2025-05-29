package project.spring_basic.service.BoardServiceTest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.PlatformTransactionManager;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import project.spring_basic.data.repository.MemberRepository;
import project.spring_basic.data.repository.PostRepository;
import project.spring_basic.service.BoardService;


@ActiveProfiles("test")
@SpringBootTest
public abstract class BoardServiceIntegrationTestSupport {
    
    @Autowired
    protected BoardService boardService;

    @Autowired
    protected PostRepository postRepository;

    @Autowired
    protected MemberRepository memberRepository;

    @PersistenceContext
    protected EntityManager entityManager;

    @Autowired
    protected PlatformTransactionManager transactionManager;
}
