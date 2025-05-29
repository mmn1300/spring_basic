package project.spring_basic.service.querys;

import project.spring_basic.data.dto.Response.Json.PostsDTO;
import project.spring_basic.data.entity.Member;
import project.spring_basic.data.entity.Post;

public interface BoardServiceQuerys {

    public Post getPost(Long postId) throws Exception;
    
    // 해당 페이지에 맞는 게시글들을 반환
    public PostsDTO getPostsInfo(int pageNum) throws Exception;

    // 게시자 별로 해당 페이지에 맞는 게시글들을 반환
    public PostsDTO getPostsInfoByUser(int pageNum, Long userAccountId, Member member) throws Exception;

    // 해당 게시자의 작성글 수 반환
    public Integer getUserPostCount(Long userId) throws Exception;
}
