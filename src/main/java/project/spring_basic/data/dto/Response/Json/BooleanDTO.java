package project.spring_basic.data.dto.Response.Json;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class BooleanDTO extends ResponseDTO {
    private Boolean data;

    public BooleanDTO(Boolean message, Boolean data){
        super(message);
        this.data = data;
    }
}
