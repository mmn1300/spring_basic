package project.spring_basic.data.dao;

import java.util.List;

import project.spring_basic.data.entity.Post;

import java.util.Optional;

public interface PostDAO {

    public Optional<Post> findById(Long postId);

    public List<Post> findByIdBetween(Long start, Long end);

    public void save(Post post);

    public void deleteById(Long postId);

    public Post findLatestPost();

    public void updateIdsGreaterThan(Long postId);


    public void updateAutoIncrement(Long lastId);

}