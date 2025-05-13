package project.spring_basic.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import jakarta.transaction.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

import project.spring_basic.data.entity.Post;
import project.spring_basic.data.repository.PostRepository;
import project.spring_basic.data.repository.PostRepositoryCustom;


@ActiveProfiles("test")
@SpringBootTest
public class PostRepositoryCustomTest {
    
    @Autowired PostRepository postRepository;

    @Autowired PostRepositoryCustom postRepositoryCustom;


    @Test
    @Transactional
    @DisplayName("Auto Increment 값을 변경한다")
    public void updateAutoIncrement(){
        // given
        Post newPost = Post.builder()
                .userId(1L)
                .title("1")
                .content("1")
                .createAt(LocalDateTime.now().withNano(0))
                .build();

        Long newAutoIncrement = 10L;

        
        // when
        postRepositoryCustom.updateAutoIncrement(newAutoIncrement);
        postRepository.save(newPost);
        List<Post> posts = postRepository.findAll();


        // then
        assertThat(posts).hasSize(1);

        assertThat(posts.get(0))
                .extracting("id")
                .isEqualTo(newAutoIncrement);
    }

}
