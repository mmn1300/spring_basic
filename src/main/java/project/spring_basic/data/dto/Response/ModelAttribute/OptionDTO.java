package project.spring_basic.data.dto.Response.ModelAttribute;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class OptionDTO {
    private UserOption userOption;

    public OptionDTO (String userId, Long id){
        if(userId.length() >= 4){
            userId = userId.substring(0, 4) + "****";
        }
        this.userOption = new UserOption(userId, id);
    }
}


@Getter
@Setter
@AllArgsConstructor
class UserOption {
    private String userId;
    private Long id;
}
