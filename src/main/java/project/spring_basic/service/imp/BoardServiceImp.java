package project.spring_basic.service.imp;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Sort;

import project.spring_basic.data.PostInfo;
import project.spring_basic.data.dao.MemberDAO;
import project.spring_basic.data.dao.PostDAO;
import project.spring_basic.data.dto.Request.PostDTO;
import project.spring_basic.data.dto.Response.Json.PostsDTO;
import project.spring_basic.data.dto.Response.ModelAttribute.PostReadDTO;
import project.spring_basic.data.dto.Response.ModelAttribute.PostUpdateDTO;
import project.spring_basic.data.entity.Post;
import project.spring_basic.data.entity.Member;
import project.spring_basic.service.BoardService;

import java.util.UUID;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
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

    @Autowired
    private MemberDAO memberDAO;

    private final Object lock = new Object();


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

            postInfo.setId(postContent.getId());

            Long userId = postContent.getUserId();
            // 유저 데이터가 해시맵에 존재하는 경우(중복 질의 방지)
            if(hashData.containsKey(userId)){
                postInfo.setUserId(hashData.get(userId).get("userStrId"));
                postInfo.setNickname(hashData.get(userId).get("nickname"));
            }
            // 유저 데이터가 해시맵에 존재하지 않는 경우
            else{
                Member member = memberDAO.findById(userId).get();

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
            postInfo.setCreateAt(postContent.getCreateAt());

            postsInfo.add(postInfo);
        }

        postsDTO.setPosts(postsInfo);

        return postsDTO;
    }


    // 게시자 별로 해당 페이지에 맞는 게시글들을 반환
    public PostsDTO getPostsInfoByUser(int pageNum, String userId) throws Exception{
        PostsDTO postsDTO = new PostsDTO();
        final int maxPost = 16;
        pageNum--;

        Member member = memberDAO.findByUserId(userId).get(0);
        PageRequest pageRequest = PageRequest.of(pageNum, maxPost);
        Page<Post> posts = postDAO.findByUserIdOrderByIdDesc(member.getId(), pageRequest);
        postsDTO.setMessage(true);
        postsDTO.setRows(Long.valueOf(posts.stream().count()).intValue()); // primitive long to int

        List<Post> postContents = posts.getContent();
        List<PostInfo> postsInfo = new ArrayList<>();

        // DB 질의 데이터를 DTO에 맞는 데이터만을 추출하여 제공
        for (Post postContent : postContents) {
            PostInfo postInfo = new PostInfo();

            postInfo.setId(postContent.getId());
            postInfo.setUserId(userId);
            postInfo.setNickname(member.getNickname());
            postInfo.setTitle(postContent.getTitle());
            postInfo.setContent(postContent.getContent());
            postInfo.setCreateAt(postContent.getCreateAt());

            postsInfo.add(postInfo);
        }

        postsDTO.setPosts(postsInfo);

        return postsDTO;
    }


    // 해당 게시자의 작성글 수 반환
    public Integer getUserPostCount(String userId) throws Exception{
        Long id = memberDAO.findByUserId(userId).get(0).getId();
        return postDAO.countByUserId(id);
    }


    // 읽기용 게시글 정보 (게시글 ID, 제목, 내용, 닉네임, 유저 ID(문자열), 생성일)
    public PostReadDTO getReadPost(Long postNum) throws Exception{
        PostReadDTO postReadDTO = new PostReadDTO();
        Post post = postDAO.findById(postNum).get();
        Member member = memberDAO.findById(post.getUserId()).get();

        postReadDTO.setNumber(postNum);
        postReadDTO.setTitle(post.getTitle());
        postReadDTO.setContent(post.getContent());
        postReadDTO.setUserId(member.getUserId());
        postReadDTO.setNickname(member.getNickname());
        postReadDTO.setCreateAt(post.getCreateAt());

        return postReadDTO;
    }


    // 수정용 게시글 정보(제목, 내용, 닉네임, 유저 ID(문자열), 파일 이름)
    public PostUpdateDTO getUpdatePost(Long postNum) throws Exception {
        PostUpdateDTO postUpdateDTO = new PostUpdateDTO();
        Post post = postDAO.findById(postNum).get();
        Member member = memberDAO.findById(post.getUserId()).get();

        postUpdateDTO.setTitle(post.getTitle());
        postUpdateDTO.setContent(post.getContent());
        postUpdateDTO.setUserId(member.getUserId());
        postUpdateDTO.setNickname(member.getNickname());
        postUpdateDTO.setFileName(post.getFileName());

        return postUpdateDTO;
    }


    // 게시글 저장
    // 동시에 여러 트랜잭션이 데이터를 삽입하는 것을 방지
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void save(PostDTO postDTO, Long userId, MultipartFile file) throws Exception {
        Post post = new Post();

        post.setUserId(userId);
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

        // 작업 수행 중 다른 스레드를 막음
        synchronized (lock) {

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
        }

        post.setUpdateAt(LocalDateTime.now());
        postDAO.save(post);
    }


    // 게시글 삭제
    @Transactional
    public void remove(Long postId) throws Exception {
        String tempName = postDAO.findById(postId).get().getTempName();

        // 작업 수행 중 다른 스레드를 막음
        synchronized (lock) {

            // 이 테이블에 대한 다른 삽입 트랜잭션을 막음
            postDAO.lockTable();

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

    }


    // 게시글 작성자 확인
    public boolean checkUser(Long postId, String memberUserId){
        Post post = postDAO.findById(postId).get();
        Member member = memberDAO.findById(post.getUserId()).get();
        if(member.getUserId().equals(memberUserId)){
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
