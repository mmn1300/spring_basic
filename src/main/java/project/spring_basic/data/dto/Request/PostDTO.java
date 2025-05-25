package project.spring_basic.data.dto.Request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class PostDTO {
    @NotBlank(message="제목이 존재해야 합니다.")
    @Size(min=1, max=200, message="제목은 1자 이상 200자 이하여야 합니다.")
    private String title;

    @NotBlank(message="내용물이 존재해야 합니다.")
    private String content;
}
