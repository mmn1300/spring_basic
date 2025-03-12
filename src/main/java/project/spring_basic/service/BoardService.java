package project.spring_basic.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import project.spring_basic.dto.Request.PostDTO;
import project.spring_basic.dto.Response.PostsDTO;
import project.spring_basic.entity.Post;
import project.spring_basic.repository.BoardRepository;

import java.util.List;


@Service
public class BoardService {
    
    @Autowired
    private BoardRepository boardRepository;


    // 해당 페이지에 맞는 게시글들을 반환
    public PostsDTO getPosts(PostsDTO postsDTO, Long pageNum) throws Exception {
        final Long maxPost = 16L;
        Long start = ((pageNum - 1L) * maxPost) + 1L;
        Long end = pageNum * maxPost;

        List<Post> posts = boardRepository.findByIdBetween(start, end);
        postsDTO.setMessage(true);
        postsDTO.setRows((int) posts.stream().count());
        postsDTO.setPosts(posts);

        return postsDTO;
    }

    // 게시글 읽기
    public Post getPost(Long postNum) throws Exception {
        return boardRepository.findById(postNum).get();
    }


    // 게시글 저장
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


    // 게시글 작성자 확인
    public boolean checkUser(Long postId, String memberUserId){
        Post post = boardRepository.findById(postId).get();
        if(post.getUserId().equals(memberUserId)){
            return true;
        }else{
            return false;
        }
    }
}
