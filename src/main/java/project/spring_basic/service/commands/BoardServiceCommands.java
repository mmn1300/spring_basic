package project.spring_basic.service.commands;

import org.springframework.web.multipart.MultipartFile;

import project.spring_basic.data.dto.Request.PostDTO;

public interface BoardServiceCommands {

    // 게시글 저장
    public void save(PostDTO postDTO, Long userId, MultipartFile file) throws Exception;

    // 게시글 수정
    public void update(Long postId, PostDTO postDTO, MultipartFile newFile) throws Exception;

    // 게시글 삭제
    public void remove(Long postId) throws Exception;
}
