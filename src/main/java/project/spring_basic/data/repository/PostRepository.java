package project.spring_basic.data.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
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

    public void lockTable(){
        entityManager.createQuery("SELECT p FROM Post p WHERE p.id IS NOT NULL")
                 .setLockMode(LockModeType.PESSIMISTIC_WRITE)
                 .setMaxResults(1)
                 .getResultList();
    }
}