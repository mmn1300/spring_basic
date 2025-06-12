package project.spring_basic.service.imp;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import project.spring_basic.constant.UserDefinePath;
import project.spring_basic.data.dto.Request.PostDTO;
import project.spring_basic.data.dto.Response.Json.PostsDTO;
import project.spring_basic.data.dto.Response.ModelAttribute.PostReadDTO;
import project.spring_basic.data.dto.Response.ModelAttribute.PostUpdateDTO;
import project.spring_basic.data.entity.Member;
import project.spring_basic.data.entity.Post;
import project.spring_basic.exception.DtoNullException;
import project.spring_basic.service.BoardService;
import project.spring_basic.service.commands.BoardServiceCommands;
import project.spring_basic.service.querys.BoardServiceQuerys;
import project.spring_basic.service.querys.MemberServiceQuerys;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class BoardServiceImp implements BoardService {

    @Autowired
    private BoardServiceCommands boardServiceCommands;

    @Autowired
    private BoardServiceQuerys boardServiceQuerys;

    @Autowired
    private MemberServiceQuerys memberServiceQuerys;


    /* 
    * 
    * 입력값에 대한 예외,
    * 책임 분리를 위한 1차 서비스 처리 계층
    * 
    */
    

    // 해당 페이지에 맞는 게시글들을 반환
    public PostsDTO getPostsInfo(int pageNum) throws Exception {
        if (pageNum <= 0) {
            throw new IllegalArgumentException("양의 정수를 입력해야 합니다.");
        }
        
        return boardServiceQuerys.getPostsInfo(pageNum);
    }


    // 게시자 별로 해당 페이지에 맞는 게시글들을 반환
    public PostsDTO getPostsInfoByUser(int pageNum, Long userAccountId) throws Exception {
        if (pageNum <= 0 || userAccountId <= 0L) {
            throw new IllegalArgumentException("양의 정수를 입력해야 합니다.");
        }

        Member member = memberServiceQuerys.getMember(userAccountId);
        return boardServiceQuerys.getPostsInfoByUser(pageNum, userAccountId, member);
    }


    // 해당 게시자의 작성글 수 반환
    public Integer getUserPostCount(String userId) throws Exception {
        Member member = memberServiceQuerys.getMemberByUserId(userId);
        return boardServiceQuerys.getUserPostCount(member.getId());
    }


    // 읽기용 게시글 정보 (게시글 ID, 제목, 내용, 닉네임, 유저 ID(문자열), 생성일)
    public PostReadDTO getReadPost(Long postNum) throws Exception{
        if (postNum <= 0) {
            throw new IllegalArgumentException("양의 정수를 입력해야 합니다.");
        }

        Post post = boardServiceQuerys.getPost(postNum);
        Member member = post.getMember();
        PostReadDTO postReadDTO = new PostReadDTO(null, null, null, null, null, null);

        postReadDTO.setNumber(postNum);
        postReadDTO.setTitle(post.getTitle());
        postReadDTO.setContent(post.getContent());
        postReadDTO.setCreateAt(postReadDTO.localDateTimeToString(post.getCreateAt()));
        postReadDTO.setUserId(member.getUserId());
        postReadDTO.setNickname(member.getNickname());
        return postReadDTO;
    }


    // 수정용 게시글 정보(제목, 내용, 닉네임, 유저 ID(문자열), 파일 이름)
    public PostUpdateDTO getUpdatePost(Long postNum) throws Exception {
        if (postNum <= 0) {
            throw new IllegalArgumentException("양의 정수를 입력해야 합니다.");
        }
        Post post = boardServiceQuerys.getPost(postNum);
        Member member = post.getMember();
        PostUpdateDTO postUpdateDTO = new PostUpdateDTO(null, null, null, null, null);

        postUpdateDTO.setTitle(post.getTitle());
        postUpdateDTO.setContent(post.getContent());
        postUpdateDTO.setFileName(post.getFileName());
        postUpdateDTO.setUserId(member.getUserId());
        postUpdateDTO.setNickname(member.getNickname());
        return postUpdateDTO;
    }


    // 게시글 작성자 확인
    public boolean checkUser(Long postId, String memberUserId) throws Exception {
        Post post = boardServiceQuerys.getPost(postId);
        Member member = post.getMember();
        if(member.getUserId().equals(memberUserId)){
            return true;
        }else{
            return false;
        }
    }


    // 파일 존재 확인
    public String isFileExists(Long postId) throws Exception {
        Post post = boardServiceQuerys.getPost(postId);
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
        Post post = boardServiceQuerys.getPost(postId);
        String tempName = post.getTempName();
        String uploadDir = UserDefinePath.ABS_PATH + UserDefinePath.FILE_STORAGE_PATH;
        String filePath = uploadDir + File.separator + tempName;

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




    // 게시글 저장
    // 동시에 여러 트랜잭션이 데이터를 삽입하는 것을 방지
    public void save(PostDTO postDTO, Long userId, MultipartFile file) throws Exception {
        if(postDTO == null){
            throw new DtoNullException("DTO가 존재하지 않습니다.");
        }
        if(userId <= 0L){
            throw new IllegalArgumentException("양의 정수를 입력해야 합니다.");
        }
        Member member = memberServiceQuerys.getMember(userId);

        Post post = Post.builder()
                .member(member)
                .title(postDTO.getTitle())
                .content(postDTO.getContent())
                .createAt(LocalDateTime.now())
                .build();
        
        // 첨부된 파일 존재시
        if(file != null){
            String uploadDir = UserDefinePath.ABS_PATH + UserDefinePath.FILE_STORAGE_PATH; // 업로드 디렉터리
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

        boardServiceCommands.save(post);
    }


    // 게시글 수정
    public void update(Long postId, PostDTO postDTO, MultipartFile newFile) throws Exception {
        if(postDTO == null){
            throw new DtoNullException("DTO가 존재하지 않습니다.");
        }
        if(postId <= 0L){
            throw new IllegalArgumentException("양의 정수를 입력해야 합니다.");
        }

        Post post = boardServiceQuerys.getPost(postId);
        post.setTitle(postDTO.getTitle());
        post.setContent(postDTO.getContent());


        boardServiceCommands.update(post, newFile);
    }


    // 게시글 삭제
    public void remove(Long postId) throws Exception {
        Post post = boardServiceQuerys.getPost(postId);
        boardServiceCommands.remove(post);
    }

}
