package project.spring_basic.dto.Response;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import project.spring_basic.entity.Post;

@Getter
@Setter
@ToString
public class PostsDTO extends ResponseDTO{
    private int rows;
    private List<Post> posts;

    public PostsDTO () {}

    public PostsDTO (Boolean message, int rows, List<Post> posts){
        super(message);
        this.rows = rows;
        this.posts = posts;
    }
}
