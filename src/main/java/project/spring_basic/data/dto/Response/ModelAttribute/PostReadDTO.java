package project.spring_basic.data.dto.Response.ModelAttribute;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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
public class PostReadDTO {
    private Long number;
    private String title;
    private String content;
    private String userId;
    private String nickname;
    private String createAt;

    public String localDateTimeToString (LocalDateTime time){
        DateTimeFormatter dtFmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return time.format(dtFmt);
    }
}
