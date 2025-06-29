package project.spring_basic.api.swaggerDto;

import java.util.List;

import org.springframework.http.HttpStatus;

import io.swagger.v3.oas.annotations.media.Schema;
import project.spring_basic.data.dto.Response.Json.BooleanDTO;


@Schema(description = "Boolean 응답을 감싸는 공통 응답 포맷")
public class SwaggerBooleanDTO {
    
    @Schema(description = "HTTP 상태 코드", example = "200")
    private int code;

    @Schema(description = "HTTP 상태", example = "OK")
    private HttpStatus status;

    @Schema(description = "메시지 목록")
    private List<String> messages;

    @Schema(description = "응답 데이터")
    private BooleanDTO data;
}
