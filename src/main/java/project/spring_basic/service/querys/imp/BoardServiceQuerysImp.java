package project.spring_basic.service.querys.imp;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import project.spring_basic.constant.UserDefinePath;
import project.spring_basic.data.PostInfo;
import project.spring_basic.data.dao.MemberDAO;
import project.spring_basic.data.dao.PostDAO;
import project.spring_basic.data.dto.Response.Json.PostsDTO;
import project.spring_basic.data.dto.Response.ModelAttribute.PostReadDTO;
import project.spring_basic.data.dto.Response.ModelAttribute.PostUpdateDTO;
import project.spring_basic.data.entity.Member;
import project.spring_basic.data.entity.Post;
import project.spring_basic.exception.MemberNotFoundException;
import project.spring_basic.exception.PostNotFoundException;
import project.spring_basic.service.querys.BoardServiceQuerys;

@Service
@Transactional(readOnly = true)
public class BoardServiceQuerysImp implements BoardServiceQuerys {
        
    @Autowired
    private PostDAO postDAO;

    @Autowired
    private MemberDAO memberDAO;


    // 해당 페이지에 맞는 게시글들을 반환
    public PostsDTO getPostsInfo(int pageNum) throws Exception {
        if (pageNum <= 0) {
            throw new IllegalArgumentException("양의 정수를 입력해야 합니다.");
        }

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

            postInfo.setId(postContent.getId());

            Long userId = postContent.getUserId();
            // 유저 데이터가 해시맵에 존재하는 경우(중복 질의 방지)
            if(hashData.containsKey(userId)){
                postInfo.setUserId(hashData.get(userId).get("userStrId"));
                postInfo.setNickname(hashData.get(userId).get("nickname"));
            }
            // 유저 데이터가 해시맵에 존재하지 않는 경우
            else{
                Member member = memberDAO.findById(userId).map(m -> m)
                    .orElseThrow(() -> new MemberNotFoundException("해당 회원은 존재하지 않습니다."));

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
    public PostsDTO getPostsInfoByUser(int pageNum, Long userAccountId) throws Exception {
        if (pageNum <= 0 || userAccountId <= 0L) {
            throw new IllegalArgumentException("양의 정수를 입력해야 합니다.");
        }

        PostsDTO postsDTO = new PostsDTO();
        final int maxPost = 16;
        pageNum--;

        Member member = memberDAO.findById(userAccountId).map(m -> m)
                .orElseThrow(() -> new MemberNotFoundException("해당 회원은 존재하지 않습니다."));
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
    public Integer getUserPostCount(String userId) throws Exception {
        List<Member> members = memberDAO.findByUserId(userId);

        if (members.size() != 1) {
            throw new MemberNotFoundException("해당 회원은 존재하지 않습니다.");
        }

        return postDAO.countByUserId(members.get(0).getId());
    }


    // 읽기용 게시글 정보 (게시글 ID, 제목, 내용, 닉네임, 유저 ID(문자열), 생성일)
    public PostReadDTO getReadPost(Long postNum) throws Exception{
        if (postNum <= 0) {
            throw new IllegalArgumentException("양의 정수를 입력해야 합니다.");
        }

        PostReadDTO postReadDTO = new PostReadDTO(null, null, null, null, null, null);
        Post post = postDAO.findById(postNum).map(p -> p)
                .orElseThrow(() -> new PostNotFoundException(postNum + "번 게시글은 존재하지 않습니다."));
        postReadDTO.setNumber(postNum);

        Member member = memberDAO.findById(post.getUserId()).map(m -> m)
                .orElseThrow(() -> new MemberNotFoundException("해당 회원은 존재하지 않습니다."));

        postReadDTO.setTitle(post.getTitle());
        postReadDTO.setContent(post.getContent());
        postReadDTO.setUserId(member.getUserId());
        postReadDTO.setNickname(member.getNickname());
        postReadDTO.setCreateAt(postReadDTO.localDateTimeToString(post.getCreateAt()));


        return postReadDTO;
    }


    // 수정용 게시글 정보(제목, 내용, 닉네임, 유저 ID(문자열), 파일 이름)
    public PostUpdateDTO getUpdatePost(Long postNum) throws Exception {
        if (postNum <= 0) {
            throw new IllegalArgumentException("양의 정수를 입력해야 합니다.");
        }

        PostUpdateDTO postUpdateDTO = new PostUpdateDTO(null, null, null, null, null);
        Post post = postDAO.findById(postNum).map(p -> p)
                .orElseThrow(() -> new PostNotFoundException(postNum + "번 게시글은 존재하지 않습니다."));

        Member member = memberDAO.findById(post.getUserId()).map(m -> m)
                .orElseThrow(() -> new MemberNotFoundException("해당 회원은 존재하지 않습니다."));

        postUpdateDTO.setTitle(post.getTitle());
        postUpdateDTO.setContent(post.getContent());
        postUpdateDTO.setUserId(member.getUserId());
        postUpdateDTO.setNickname(member.getNickname());
        postUpdateDTO.setFileName(post.getFileName());


        return postUpdateDTO;
    }


    // 게시글 작성자 확인
    public boolean checkUser(Long postId, String memberUserId){
        Post post = postDAO.findById(postId)
            .orElseThrow(() -> new PostNotFoundException(postId + "번 게시글은 존재하지 않습니다."));

        Member member = memberDAO.findById(post.getUserId()).map(m -> m)
                .orElseThrow(() -> new MemberNotFoundException("해당 회원은 존재하지 않습니다."));
        if(member.getUserId().equals(memberUserId)){
            return true;
        }else{
            return false;
        }
    }


    // 파일 존재 확인
    public String isFileExists(Long postId) throws Exception {
        Post post = postDAO.findById(postId)
            .orElseThrow(() -> new PostNotFoundException(postId + "번 게시글은 존재하지 않습니다."));

        String tempName = post.getTempName();
        if (tempName == null || tempName.isEmpty()) {
            return "";
        }

        Path filePath = Paths.get(UserDefinePath.ABS_PATH, UserDefinePath.FILE_STORAGE_PATH, tempName);
        if (Files.exists(filePath)) {
            return post.getFileName();
        }

        return "";
    }


    // 서버에 저장되어있는 파일 가져오기
    public ResponseEntity<Object> getFile(Long postId) throws Exception {
        Post post = postDAO.findById(postId)
            .orElseThrow(() -> new PostNotFoundException(postId + "번 게시글은 존재하지 않습니다."));
        String tempName = post.getTempName();
        String uploadDir = UserDefinePath.ABS_PATH + UserDefinePath.FILE_STORAGE_PATH;
        String filePath = uploadDir + '\\' + tempName;

        Path path = Paths.get(filePath);
        if (Files.exists(path) && Files.isRegularFile(path)) {
            Resource resource = new FileSystemResource(path);
            HttpHeaders headers = new HttpHeaders();
            // 클라이언트에게 파일을 다운로드로 처리하라고 지시 attachment를 통해 파일 다운로드를 유도
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + post.getFileName());
            // 응답형태 명시. application/octet-stream는 바이너리 데이터를 나타내는 MIME 타입 (파일)
            headers.add(HttpHeaders.CONTENT_TYPE, "application/octet-stream");

            return ResponseEntity.ok()
                .headers(headers)
                .body(resource);
        }else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("File not found");
        }
    }
}
