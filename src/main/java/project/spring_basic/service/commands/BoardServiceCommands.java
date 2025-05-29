package project.spring_basic.service.commands;

import org.springframework.web.multipart.MultipartFile;

import project.spring_basic.data.entity.Post;

public interface BoardServiceCommands {

    // 게시글 저장
    public void save(Post post) throws Exception;

    // 게시글 수정
    public void update(Post post, MultipartFile newFile) throws Exception;

    // 게시글 삭제
    public void remove(Post post) throws Exception;
}
