package project.spring_basic.data.dto.Response;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ErrorDTO extends ResponseDTO {
    private String error;

    public ErrorDTO(Boolean message, String error) {
        super(message);
        this.error = error;
    }
}
