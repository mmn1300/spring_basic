package project.spring_basic.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import project.spring_basic.dto.Request.PostDTO;
import project.spring_basic.entity.Post;
import project.spring_basic.repository.BoardRepository;

@Service
public class BoardService {
    
    @Autowired
    private BoardRepository boardRepository;

    public void save(PostDTO postDTO, String userId, String nickname) throws Exception {
        Post post = new Post();

        post.setUserId(userId);
        post.setNickname(nickname);
        post.setTitle(postDTO.getTitle());
        post.setContent(postDTO.getContent());
        post.setCreateAt(LocalDateTime.now());
        post.setUpdateAt(null);
        post.setFileName(null);
        post.setFileType(null);
        post.setTempName(null);

        boardRepository.save(post);
    }
}
