package project.spring_basic.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import project.spring_basic.data.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Repository
public interface PostRepository extends JpaRepository<Post, Long>{

    @Modifying
    @Query("UPDATE Post p SET p.id = p.id - 1 WHERE p.id > :threshold")
    void updateIdsGreaterThan(@Param("threshold") Long threshold);

    @Query(value = "SELECT * FROM posts ORDER BY id DESC LIMIT 1", nativeQuery = true)
    Post findLatestPost();

    Page<Post> findByMemberIdOrderByIdDesc(Long userId, Pageable pageable);

    Long countByMemberId(Long userId);

    Boolean existsByMemberId(Long userId);
}
