package project.spring_basic.service.querys;

import org.springframework.http.ResponseEntity;

import project.spring_basic.data.dto.Response.Json.PostsDTO;
import project.spring_basic.data.dto.Response.ModelAttribute.PostReadDTO;
import project.spring_basic.data.dto.Response.ModelAttribute.PostUpdateDTO;

public interface BoardServiceQuerys {
    
    // 해당 페이지에 맞는 게시글들을 반환
    public PostsDTO getPostsInfo(int pageNum) throws Exception;

    // 게시자 별로 해당 페이지에 맞는 게시글들을 반환
    public PostsDTO getPostsInfoByUser(int pageNum, Long userAccountId) throws Exception;

    // 해당 게시자의 작성글 수 반환
    public Integer getUserPostCount(String userId) throws Exception;

    // 읽기용 게시글 정보 (게시글 ID, 제목, 내용, 닉네임, 유저 ID(문자열), 생성일)
    public PostReadDTO getReadPost(Long postNum) throws Exception;

    // 수정용 게시글 정보 (제목, 내용, 닉네임, 유저 ID(문자열), 파일 이름)
    public PostUpdateDTO getUpdatePost(Long postNum) throws Exception;

    // 게시글 작성자 확인
    public boolean checkUser(Long postId, String memberUserId) throws Exception;

    // 파일 존재 확인
    public String isFileExists(Long postId) throws Exception;

    // 서버에 저장되어있는 파일 가져오기
    public ResponseEntity<Object> getFile(Long postId) throws Exception;
}
