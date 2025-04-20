package project.spring_basic.data.dto.Response.ModelAttribute;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.NoArgsConstructor;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class PostUpdateDTO {
    private String title;
    private String content;
    private String userId;
    private String nickname;
    private String fileName;
}
