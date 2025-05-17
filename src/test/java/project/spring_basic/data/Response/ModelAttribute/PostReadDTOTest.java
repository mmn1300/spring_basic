package project.spring_basic.data.Response.ModelAttribute;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import project.spring_basic.data.dto.Response.ModelAttribute.PostReadDTO;


@Tag("unit")
@SpringBootTest
public class PostReadDTOTest {
    
    @Test
    @DisplayName("")
    public void localDateTimeToString() {

        // given
        LocalDateTime time = LocalDateTime.of(2025, 5, 14, 15, 30, 2);
        String expectedString = "2025-05-14 15:30:02";
        PostReadDTO postReadDTO = new PostReadDTO();

        // when
        String formattedTime = postReadDTO.localDateTimeToString(time);

        // then
        assertThat(formattedTime).isEqualTo(expectedString);
    }
}
