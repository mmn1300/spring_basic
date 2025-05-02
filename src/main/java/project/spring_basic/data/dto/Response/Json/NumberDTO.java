package project.spring_basic.data.dto.Response.Json;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NumberDTO extends ResponseDTO {
    private Integer num;

    public NumberDTO(Boolean message, Integer num){
        super(message);
        this.num = num;
    }
}
