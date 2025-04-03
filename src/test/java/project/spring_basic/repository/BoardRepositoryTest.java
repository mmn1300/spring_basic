// package project.spring_basic.repository;

// import org.junit.jupiter.api.Test;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.test.context.SpringBootTest;
// import org.springframework.data.domain.Page;
// import org.springframework.data.domain.PageRequest;
// import org.springframework.data.domain.Sort;

// import project.spring_basic.data.entity.Post;
// import project.spring_basic.data.repository.BoardRepository;

// @SpringBootTest
// public class BoardRepositoryTest {
    
//     @Autowired BoardRepository boardRepository;

//     @Test
//     public void printPosts(){
//         final int pageNum = 0;
//         final int maxPost = 16;

//         PageRequest pageRequest = PageRequest.of(pageNum, maxPost, Sort.by(Sort.Order.desc("id")));

//         Page<Post> posts = boardRepository.findAll(pageRequest);


//         System.out.println();
//         System.out.println("=================================");
//         System.out.println();

//         for (Post post : posts.getContent()){
//             System.out.println(post.toString());
//         }

//         System.out.println();
//         System.out.println("=================================");
//         System.out.println();
//     }
// }
