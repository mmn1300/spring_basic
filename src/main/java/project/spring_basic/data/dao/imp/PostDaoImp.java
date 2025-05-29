package project.spring_basic.data.dao.imp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import project.spring_basic.data.dao.PostDAO;
import project.spring_basic.data.entity.Post;

import project.spring_basic.data.repository.PostRepository;
import project.spring_basic.data.repository.PostRepositoryCustom;

import java.util.Optional;


@Service
public class PostDaoImp implements PostDAO{
    
    @Autowired
    private PostRepository postRepository;

    @Autowired
    private PostRepositoryCustom postRepositoryCustom;


    public Optional<Post> findById(Long postId){
        return postRepository.findById(postId);
    }

    public Page<Post> findAll(Pageable pageable){
        return postRepository.findAll(pageable);
    }

    public Page<Post> findByUserIdOrderByIdDesc(Long userId, Pageable pageable){
        return postRepository.findByMemberIdOrderByIdDesc(userId, pageable);
    }

    public Integer countByUserId(Long userId){
        return Long.valueOf(postRepository.countByMemberId(userId)).intValue();
    }

    public void save(Post post){
        postRepository.save(post);
    }

    public void deleteById(Long postId){
        postRepository.deleteById(postId);
    }

    public Post findLatestPost(){
        return postRepository.findLatestPost();
    }

    public void updateIdsGreaterThan(Long postId){
        postRepository.updateIdsGreaterThan(postId);
    }
    


    public void updateAutoIncrement(Long lastId){
        postRepositoryCustom.updateAutoIncrement(lastId);
    }

    public void lockTable(){
        postRepositoryCustom.lockTable();
    }
}
