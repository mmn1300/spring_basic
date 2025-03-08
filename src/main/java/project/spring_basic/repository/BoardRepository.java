package project.spring_basic.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.spring_basic.entity.Post;

@Repository
public interface BoardRepository extends JpaRepository<Post, Long>{
    List<Post> findByIdBetween(Long startId, Long endId);
}
