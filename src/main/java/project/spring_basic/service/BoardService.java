package project.spring_basic.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.stereotype.Service;

import project.spring_basic.dto.Request.PostDTO;
import project.spring_basic.dto.Response.PostsDTO;

import project.spring_basic.entity.Post;
import project.spring_basic.repository.BoardRepository;
import project.spring_basic.repository.PostRepository;

import java.util.List;
import java.util.UUID;
import java.io.File;

@Service
public class BoardService {
    
    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private PostRepository postRepository;


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
    public void save(PostDTO postDTO, String userId, String nickname, MultipartFile file) throws Exception {
        Post post = new Post();

        post.setUserId(userId);
        post.setNickname(nickname);
        post.setTitle(postDTO.getTitle());
        post.setContent(postDTO.getContent());
        post.setCreateAt(LocalDateTime.now());
        post.setUpdateAt(null);
        
        if(file == null){
            post.setFileName(null);
            post.setFileType(null);
            post.setTempName(null);
        }else{
            String absPath = System.getProperty("user.dir");
            String uploadDir = absPath + "\\src\\main\\resources\\static\\files"; // 업로드 디렉터리
            String fileName = file.getOriginalFilename();
            if(fileName != null){
                String fileType = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
                UUID uuid = UUID.randomUUID();
                String tempName = uuid.toString() + '.' + fileType;
                File targetFile = new File(uploadDir, tempName);
                file.transferTo(targetFile);

                post.setFileName(fileName);
                post.setFileType(fileType);
                post.setTempName(tempName);
            }
        }

        boardRepository.save(post);
    }


    // 게시글 수정
    public void update(PostDTO postDTO, Long postId) throws Exception{
        Post post = boardRepository.findById(postId).get();

        post.setTitle(postDTO.getTitle());
        post.setContent(postDTO.getContent());
        post.setUpdateAt(LocalDateTime.now());

        boardRepository.save(post);
    }


    // 게시글 삭제
    @Transactional
    public void remove(Long postId) throws Exception {
        boardRepository.deleteById(postId);
        Long lastId = boardRepository.findLatestPost().getId();
        if(lastId > postId){
            boardRepository.updateIdsGreaterThan(postId);
        }
        postRepository.updateAutoIncrement(lastId);
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

    // 파일 존재 확인
    public String isFileExists(Long postId) throws Exception{
        Post post = boardRepository.findById(postId).get();
        if(post.getTempName() != null){
            String absPath = System.getProperty("user.dir");
            String uploadDir = absPath + "\\src\\main\\resources\\static\\files";
            File file = new File(uploadDir + '\\' + post.getTempName());
            if (file.exists()) {
                return post.getFileName();
            } else {
                return "";
            }
        }
        return "";
    }
}
