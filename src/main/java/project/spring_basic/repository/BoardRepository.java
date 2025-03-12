package project.spring_basic.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import project.spring_basic.entity.Post;

@Repository
public interface BoardRepository extends JpaRepository<Post, Long>{
    List<Post> findByIdBetween(Long startId, Long endId);

    @Modifying
    @Query("UPDATE Post p SET p.id = p.id - 1 WHERE p.id > :threshold")
    void updateIdsGreaterThan(Long threshold);

    @Query(value = "SELECT * FROM posts ORDER BY id DESC LIMIT 1", nativeQuery = true)
    Post findLatestPost();
}
