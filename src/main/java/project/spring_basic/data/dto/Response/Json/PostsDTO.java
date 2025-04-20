package project.spring_basic.data.dto.Response.Json;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import project.spring_basic.data.PostInfo;

@Getter
@Setter
@ToString
public class PostsDTO extends ResponseDTO{
    private int rows;
    private List<PostInfo> posts;

    public PostsDTO () {}

    public PostsDTO (Boolean message, int rows, List<PostInfo> posts){
        super(message);
        this.rows = rows;
        this.posts = posts;
    }
}
