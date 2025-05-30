package project.spring_basic.service.querys.imp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import project.spring_basic.data.PostInfo;
import project.spring_basic.data.dao.PostDAO;
import project.spring_basic.data.dto.Response.Json.PostsDTO;
import project.spring_basic.data.entity.Member;
import project.spring_basic.data.entity.Post;
import project.spring_basic.exception.PostNotFoundException;
import project.spring_basic.service.querys.BoardServiceQuerys;

@Service
@Transactional(readOnly = true)
public class BoardServiceQuerysImp implements BoardServiceQuerys {
        
    @Autowired
    private PostDAO postDAO;


    /* 
    * 
    * 테이블 작업 중 발생하는 예외에 대한 처리,
    * 테이블에 대한 직접적인 작업을 수행하는 2차 서비스 처리 계층
    * 
    */

    public Post getPost(Long postId) throws Exception {
        Post post = postDAO.findById(postId).map(p -> p)
            .orElseThrow(() -> new PostNotFoundException(postId + "번 게시글은 존재하지 않습니다."));
        return post;
    }


    // 해당 페이지에 맞는 게시글들을 반환
    public PostsDTO getPostsInfo(int pageNum) throws Exception {
        PostsDTO postsDTO = new PostsDTO();
        final int maxPost = 16;
        pageNum--;

        PageRequest pageRequest = PageRequest.of(pageNum, maxPost, Sort.by(Sort.Order.desc("id")));
        Page<Post> posts = postDAO.findAll(pageRequest);
        postsDTO.setMessage(true);
        postsDTO.setRows((int) posts.stream().count());

        List<Post> postContents = posts.getContent();
        List<PostInfo> postsInfo = new ArrayList<>();

        Map<Long, Map<String, String>> hashData = new HashMap<>();

        // DB 질의 데이터를 DTO에 맞는 데이터만을 추출하여 제공
        for (Post postContent : postContents) {
            PostInfo postInfo = new PostInfo();
            Long postId = postContent.getId();
            Long userId = postContent.getMember().getId();

            postInfo.setId(postId);

            // 유저 데이터가 해시맵에 존재하는 경우(중복 질의 방지)
            if(hashData.containsKey(userId)){
                postInfo.setUserId(hashData.get(userId).get("userStrId"));
                postInfo.setNickname(hashData.get(userId).get("nickname"));
            }
            // 유저 데이터가 해시맵에 존재하지 않는 경우
            else{
                Member member = getPost(postId).getMember();

                // 새 유저 정보 해시맵 등록
                Map<String, String> memberInfo = new HashMap<>();
                memberInfo.put("userStrId", member.getUserId());
                memberInfo.put("nickname", member.getNickname());
                hashData.put(userId, memberInfo);

                postInfo.setUserId(member.getUserId());
                postInfo.setNickname(member.getNickname());
            }

            postInfo.setTitle(postContent.getTitle());
            postInfo.setContent(postContent.getContent());
            postInfo.setCreateAt(postInfo.localDateTimeToString(postContent.getCreateAt()));

            postsInfo.add(postInfo);
        }

        postsDTO.setPosts(postsInfo);

        return postsDTO;
    }


    // 게시자 별로 해당 페이지에 맞는 게시글들을 반환
    public PostsDTO getPostsInfoByUser(int pageNum, Long userAccountId, Member member) throws Exception {
        PostsDTO postsDTO = new PostsDTO();
        final int maxPost = 16;
        pageNum--;

        PageRequest pageRequest = PageRequest.of(pageNum, maxPost);
        Page<Post> posts = postDAO.findByUserIdOrderByIdDesc(userAccountId, pageRequest);
        postsDTO.setMessage(true);
        postsDTO.setRows(Long.valueOf(posts.stream().count()).intValue()); // primitive long to int

        List<Post> postContents = posts.getContent();
        List<PostInfo> postsInfo = new ArrayList<>();

        // DB 질의 데이터를 DTO에 맞는 데이터만을 추출하여 제공
        for (Post postContent : postContents) {
            PostInfo postInfo = new PostInfo();

            postInfo.setId(postContent.getId());
            postInfo.setUserId(member.getUserId());
            postInfo.setNickname(member.getNickname());
            postInfo.setTitle(postContent.getTitle());
            postInfo.setContent(postContent.getContent());
            postInfo.setCreateAt(postInfo.localDateTimeToString(postContent.getCreateAt()));

            postsInfo.add(postInfo);
        }

        postsDTO.setPosts(postsInfo);

        return postsDTO;
    }


    // 해당 게시자의 작성글 수 반환
    public Integer getUserPostCount(Long userId) throws Exception {
        return postDAO.countByUserId(userId);
    }
}
