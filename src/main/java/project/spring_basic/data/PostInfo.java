package project.spring_basic.data;

import java.time.LocalDateTime;

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
public class PostInfo {
    private Long id;
    private String userId;
    private String nickname;
    private String title;
    private String content;
    private LocalDateTime createAt;
}
