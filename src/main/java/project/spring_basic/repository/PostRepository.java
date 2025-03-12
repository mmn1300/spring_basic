package project.spring_basic.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

@Repository
public class PostRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public void updateAutoIncrement(long newAutoIncrementValue) {
        String sql = "ALTER TABLE posts AUTO_INCREMENT = :autoIncrementValue";
        entityManager.createNativeQuery(sql)
                     .setParameter("autoIncrementValue", newAutoIncrementValue)
                     .executeUpdate();
    }
}