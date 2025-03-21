package project.spring_basic.data.dto.Response;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class GeneralPurposeDTO extends ResponseDTO {
    private Integer intData;
    private String strData;
    private Boolean boolData;
    private Float floatData;

    public GeneralPurposeDTO(Boolean message, int intData, String strData, Boolean boolData, Float floatData){
        super(message);
        this.intData = intData;
        this.strData = strData;
        this.boolData = boolData;
        this.floatData = floatData;
    }
}
