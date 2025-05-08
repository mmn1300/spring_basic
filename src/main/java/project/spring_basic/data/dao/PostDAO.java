package project.spring_basic.data.dao;

import project.spring_basic.data.entity.Post;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PostDAO {

    public Optional<Post> findById(Long postId);

    public Page<Post> findAll(Pageable pageable);

    public Page<Post> findByUserIdOrderByIdDesc(Long userId, Pageable pageable);

    public Integer countByUserId(Long userId);

    public void save(Post post);

    public void deleteById(Long postId);

    public Post findLatestPost();

    public void updateIdsGreaterThan(Long postId);

    public void updateAutoIncrement(Long lastId);

    public void lockTable();
}