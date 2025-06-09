package project.spring_basic.service.commands.imp;

import java.io.File;
import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import project.spring_basic.constant.UserDefinePath;
import project.spring_basic.data.dao.PostDAO;
import project.spring_basic.data.entity.Post;
import project.spring_basic.service.commands.BoardServiceCommands;

@Service
@Transactional
public class BoardServiceCommandsImp implements BoardServiceCommands {

    @Autowired
    private PostDAO postDAO;

    private final Object lock = new Object();


    /* 
    * 
    * 테이블 작업 중 발생하는 예외에 대한 처리,
    * 테이블에 대한 직접적인 작업을 수행하는 2차 서비스 처리 계층
    * 
    */

    

    // 게시글 저장
    // 동시에 여러 트랜잭션이 데이터를 삽입하는 것을 방지
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void save(Post post) throws Exception {
        postDAO.save(post);
    }


    // 게시글 수정
    public void update(Post post, MultipartFile newFile) throws Exception {
        // 작업 수행 중 다른 스레드를 막음
        synchronized (lock) {

            // 첨부된 파일 존재시
            if(newFile != null){
                String tempName = post.getTempName();
                String uploadDir = UserDefinePath.ABS_PATH + UserDefinePath.FILE_STORAGE_PATH;
                
                // 기존 파일이 존재하는지 확인
                if(tempName != null){
                    File file = new File(uploadDir + File.separator + tempName);
    
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
    }


    // 게시글 삭제
    public void remove(Post post) throws Exception {
        String tempName = post.getTempName();
        Long postId = post.getId();

        // 작업 수행 중 다른 스레드를 막음
        synchronized (lock) {

            // 이 테이블에 대한 다른 삽입 트랜잭션을 막음
            postDAO.lockTable();

            if(tempName != null){
                // 서버에 존재하는 파일 제거
                String uploadDir = UserDefinePath.ABS_PATH + UserDefinePath.FILE_STORAGE_PATH;
                File file = new File(uploadDir + File.separator + tempName);
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
}
