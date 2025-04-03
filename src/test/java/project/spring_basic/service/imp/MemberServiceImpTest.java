// package project.spring_basic.service.imp;

// import static org.junit.jupiter.api.Assertions.assertTrue;
// import static org.mockito.Mockito.when;

// import org.junit.jupiter.api.Test;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.test.context.SpringBootTest;
// import org.springframework.test.context.bean.override.mockito.MockitoBean;

// import project.spring_basic.data.dao.MemberDAO;

// @SpringBootTest  // Spring Boot의 테스트 환경을 설정
// public class MemberServiceImpTest {

//     @MockitoBean
//     private MemberDAO memberDAO;

//     @Autowired
//     private MemberServiceImp memberServiceImp;

//     @Test
//     public void memberExistsByIdText() throws Exception {
//         // given
//         String userId = "tttttttt";
//         when(memberDAO.existsByUserId(userId)).thenReturn(true);

//         // when
//         boolean result = memberServiceImp.memberExistsById(userId);

//         // then
//         assertTrue(result);
//     }
// }
