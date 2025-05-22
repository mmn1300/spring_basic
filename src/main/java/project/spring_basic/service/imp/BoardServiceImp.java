package project.spring_basic.service.imp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

import project.spring_basic.data.dto.Request.PostDTO;
import project.spring_basic.data.dto.Response.Json.PostsDTO;
import project.spring_basic.data.dto.Response.ModelAttribute.PostReadDTO;
import project.spring_basic.data.dto.Response.ModelAttribute.PostUpdateDTO;
import project.spring_basic.service.BoardService;
import project.spring_basic.service.commands.BoardServiceCommands;
import project.spring_basic.service.querys.BoardServiceQuerys;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class BoardServiceImp implements BoardService {

    @Autowired
    private BoardServiceCommands boardServiceCommands;

    @Autowired
    private BoardServiceQuerys boardServiceQuerys;
    

    // 해당 페이지에 맞는 게시글들을 반환
    public PostsDTO getPostsInfo(int pageNum) throws Exception {
        return boardServiceQuerys.getPostsInfo(pageNum);
    }


    // 게시자 별로 해당 페이지에 맞는 게시글들을 반환
    public PostsDTO getPostsInfoByUser(int pageNum, Long userAccountId) throws Exception {
        return boardServiceQuerys.getPostsInfoByUser(pageNum, userAccountId);
    }


    // 해당 게시자의 작성글 수 반환
    public Integer getUserPostCount(String userId) throws Exception {
        return boardServiceQuerys.getUserPostCount(userId);
    }


    // 읽기용 게시글 정보 (게시글 ID, 제목, 내용, 닉네임, 유저 ID(문자열), 생성일)
    public PostReadDTO getReadPost(Long postNum) throws Exception{
        return boardServiceQuerys.getReadPost(postNum);
    }


    // 수정용 게시글 정보(제목, 내용, 닉네임, 유저 ID(문자열), 파일 이름)
    public PostUpdateDTO getUpdatePost(Long postNum) throws Exception {
        return boardServiceQuerys.getUpdatePost(postNum);
    }


    // 게시글 작성자 확인
    public boolean checkUser(Long postId, String memberUserId) throws Exception {
        return boardServiceQuerys.checkUser(postId, memberUserId);
    }


    // 파일 존재 확인
    public String isFileExists(Long postId) throws Exception {
        return boardServiceQuerys.isFileExists(postId);
    }


    // 서버에 저장되어있는 파일 가져오기
    public ResponseEntity<Object> getFile(Long postId) throws Exception {
        return boardServiceQuerys.getFile(postId);
    }




    // 게시글 저장
    // 동시에 여러 트랜잭션이 데이터를 삽입하는 것을 방지
    public void save(PostDTO postDTO, Long userId, MultipartFile file) throws Exception {
        boardServiceCommands.save(postDTO, userId, file);
    }


    // 게시글 수정
    public void update(Long postId, PostDTO postDTO, MultipartFile newFile) throws Exception {
        boardServiceCommands.update(postId, postDTO, newFile);
    }


    // 게시글 삭제
    public void remove(Long postId) throws Exception {
        boardServiceCommands.remove(postId);
    }

}
