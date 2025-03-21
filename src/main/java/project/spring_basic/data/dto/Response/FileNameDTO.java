package project.spring_basic.data.dto.Response;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class FileNameDTO extends ResponseDTO {
    private String fileName;

    public FileNameDTO(Boolean message, String fileName){
        super(message);
        this.fileName = fileName;
    }
}
