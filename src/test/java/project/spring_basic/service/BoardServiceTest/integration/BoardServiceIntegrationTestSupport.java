package project.spring_basic.service.BoardServiceTest.integration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.PlatformTransactionManager;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import project.spring_basic.data.repository.PostRepository;
import project.spring_basic.service.BoardService;


@ActiveProfiles("test")
@SpringBootTest
public abstract class BoardServiceIntegrationTestSupport {
    
    @Autowired BoardService boardService;

    @Autowired PostRepository postRepository;

    @PersistenceContext EntityManager entityManager;

    @Autowired PlatformTransactionManager transactionManager;
}
