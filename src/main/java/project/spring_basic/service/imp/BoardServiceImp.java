package project.spring_basic.service.imp;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Sort;

import project.spring_basic.data.dao.PostDAO;
import project.spring_basic.data.dto.Request.PostDTO;
import project.spring_basic.data.dto.Response.PostsDTO;
import project.spring_basic.data.entity.Post;
import project.spring_basic.service.BoardService;

import java.util.UUID;
import java.io.File;
import java.nio.file.Paths;
import java.nio.file.Files;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpHeaders;


@Service
public class BoardServiceImp implements BoardService {
    
    @Autowired
    private PostDAO postDAO;


    // 해당 페이지에 맞는 게시글들을 반환
    public PostsDTO getPosts(PostsDTO postsDTO, int pageNum) throws Exception {
        final int maxPost = 16;
        pageNum--;

        Page<Post> posts = postDAO.findAll(PageRequest.of(pageNum, maxPost, Sort.by(Sort.Order.desc("id"))));
        postsDTO.setMessage(true);
        postsDTO.setRows((int) posts.stream().count());
        postsDTO.setPosts(posts.getContent());

        return postsDTO;
    }

    // 게시글 읽기
    public Post getPost(Long postNum) throws Exception {
        return postDAO.findById(postNum).get();
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
        
        // 첨부된 파일 미존재시
        if(file == null){
            post.setFileName(null);
            post.setFileType(null);
            post.setTempName(null);
        }
        // 첨부된 파일 존재시
        else{
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

        postDAO.save(post);
    }


    // 게시글 수정
    public void update(Long postId, PostDTO postDTO, MultipartFile newFile) throws Exception{
        Post post = postDAO.findById(postId).get();

        post.setTitle(postDTO.getTitle());
        post.setContent(postDTO.getContent());
        post.setUpdateAt(LocalDateTime.now());

        // 첨부된 파일 존재시
        if(newFile != null){
            String tempName = post.getTempName();
            String absPath = System.getProperty("user.dir");
            String uploadDir = absPath + "\\src\\main\\resources\\static\\files";
            
            // 기존 파일이 존재하는지 확인
            if(tempName != null){
                File file = new File(uploadDir + '\\' + tempName);

                // 존재시 기존 파일 제거
                if(file.exists()){
                    file.delete();
                    post.setFileName(null);
                    post.setFileType(null);
                    post.setTempName(null);
                }
            }

            // 파일 저장
            String fileName = newFile.getOriginalFilename();
            if(fileName != null){
                String fileType = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
                UUID uuid = UUID.randomUUID();
                String newTempName = uuid.toString() + '.' + fileType;
                File targetFile = new File(uploadDir, newTempName);
                newFile.transferTo(targetFile);
    
                post.setFileName(fileName);
                post.setFileType(fileType);
                post.setTempName(newTempName);
            }
        }

        postDAO.save(post);
    }


    // 게시글 삭제
    @Transactional
    public void remove(Long postId) throws Exception {
        String tempName = postDAO.findById(postId).get().getTempName();
        if(tempName != null){
            // 서버에 존재하는 파일 제거
            String absPath = System.getProperty("user.dir");
            String uploadDir = absPath + "\\src\\main\\resources\\static\\files";
            File file = new File(uploadDir + '\\' + tempName);
            file.delete();
        }

        // DB 게시물 제거
        postDAO.deleteById(postId);

        // 삭제 게시물 이후 번호들 앞당기기
        Long lastId = postDAO.findLatestPost().getId();
        if(lastId > postId){
            postDAO.updateIdsGreaterThan(postId);
        }

        // Auto Increment 초기화
        postDAO.updateAutoIncrement(lastId);
    }


    // 게시글 작성자 확인
    public boolean checkUser(Long postId, String memberUserId){
        Post post = postDAO.findById(postId).get();
        if(post.getUserId().equals(memberUserId)){
            return true;
        }else{
            return false;
        }
    }

    // 파일 존재 확인
    public String isFileExists(Long postId) throws Exception{
        Post post = postDAO.findById(postId).get();
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

    // 서버에 저장되어있는 파일 가져오기
    public ResponseEntity<?> getFile(Long postId) throws Exception {
        Post post = postDAO.findById(postId).get();
        String tempName = post.getTempName();
        String absPath = System.getProperty("user.dir");
        String uploadDir = absPath + "\\src\\main\\resources\\static\\files";
        String filePath = uploadDir + '\\' + tempName;

        java.nio.file.Path path = Paths.get(filePath);
        if (Files.exists(path) && Files.isRegularFile(path)) {
            Resource resource = new FileSystemResource(path);
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + post.getFileName());

            return ResponseEntity.ok()
                .headers(headers)
                .body(resource);
        }else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("File not found");
        }
    }
}
