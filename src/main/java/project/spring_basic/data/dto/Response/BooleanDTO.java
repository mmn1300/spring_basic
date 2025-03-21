package project.spring_basic.data.dto.Response;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class BooleanDTO extends ResponseDTO {
    private Boolean boolData;

    public BooleanDTO(Boolean message, Boolean boolData){
        super(message);
        this.boolData = boolData;
    }
}
