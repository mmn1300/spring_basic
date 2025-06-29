package project.spring_basic.api.swaggerDto;

import java.util.List;

import org.springframework.http.HttpStatus;

import io.swagger.v3.oas.annotations.media.Schema;
import project.spring_basic.data.dto.Response.Json.ResponseDTO;

@Schema(description = "데이터가 존재하지 않는 형식의 공통 응답 포맷")
public class SwaggerNullDTO {
    
    @Schema(description = "HTTP 상태 코드", example = "200")
    private int code;

    @Schema(description = "HTTP 상태", example = "OK")
    private HttpStatus status;

    @Schema(description = "메시지 목록")
    private List<String> messages;

    @Schema(description = "응답 데이터", nullable = true, example = "null")
    private ResponseDTO data;
}
