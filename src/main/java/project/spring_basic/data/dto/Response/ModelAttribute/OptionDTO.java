package project.spring_basic.data.dto.Response.ModelAttribute;

import java.util.Optional;

import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
public class OptionDTO {
    private UserOption userOption;

    public OptionDTO (String userId, Long id){
        userId = Optional.ofNullable(userId)
            .map(str -> str.length() > 4 ? str.substring(0, 4) + "****" : str)
            .orElse(null);
        this.userOption = new UserOption(userId, id);
    }


    @Getter
    @AllArgsConstructor
    public static class UserOption {
        private String userId;
        private Long id;
    }
}