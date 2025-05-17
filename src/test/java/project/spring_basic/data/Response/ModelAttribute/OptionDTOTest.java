package project.spring_basic.data.Response.ModelAttribute;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import project.spring_basic.data.dto.Response.ModelAttribute.OptionDTO;


@Tag("unit")
@SpringBootTest
public class OptionDTOTest {
    
    @Test
    @DisplayName("생성자에서 문자열 아이디 값을 암호화한다.")
    public void encryptUserIdInConstructor() {

        // given
        String userId = "abcdefgh";
        String encId = "abcd****";

        OptionDTO optionDTO = new OptionDTO(userId, null);

        // when
        assertThat(optionDTO.getUserOption().getUserId()).isEqualTo(encId);
        assertThat(optionDTO.getUserOption().getId()).isEqualTo(null);
    }
}
