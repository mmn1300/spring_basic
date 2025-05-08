package project.spring_basic.data.dao.imp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import project.spring_basic.data.dao.PostDAO;
import project.spring_basic.data.entity.Post;

import project.spring_basic.data.repository.BoardRepository;
import project.spring_basic.data.repository.PostRepository;

import java.util.Optional;


@Service
public class PostDaoImp implements PostDAO{
    
    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private PostRepository postRepository;


    public Optional<Post> findById(Long postId){
        return boardRepository.findById(postId);
    }

    public Page<Post> findAll(Pageable pageable){
        return boardRepository.findAll(pageable);
    }

    public Page<Post> findByUserIdOrderByIdDesc(Long userId, Pageable pageable){
        return boardRepository.findByUserIdOrderByIdDesc(userId, pageable);
    }

    public Integer countByUserId(Long userId){
        return Long.valueOf(boardRepository.countByUserId(userId)).intValue();
    }

    public void save(Post post){
        boardRepository.save(post);
    }

    public void deleteById(Long postId){
        boardRepository.deleteById(postId);
    }

    public Post findLatestPost(){
        return boardRepository.findLatestPost();
    }

    public void updateIdsGreaterThan(Long postId){
        boardRepository.updateIdsGreaterThan(postId);
    }
    


    public void updateAutoIncrement(Long lastId){
        postRepository.updateAutoIncrement(lastId);
    }

    public void lockTable(){
        postRepository.lockTable();
    }
}
