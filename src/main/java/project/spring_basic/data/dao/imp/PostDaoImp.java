package project.spring_basic.data.dao.imp;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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

    public List<Post> findByIdBetween(Long start, Long end){
        return boardRepository.findByIdBetween(start, end);
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
}
