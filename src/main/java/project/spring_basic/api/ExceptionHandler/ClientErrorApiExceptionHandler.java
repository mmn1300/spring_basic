package project.spring_basic.api.ExceptionHandler;

import org.springframework.validation.BindException;

import java.nio.file.AccessDeniedException;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.csrf.InvalidCsrfTokenException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import project.spring_basic.api.ApiResponse;
import project.spring_basic.data.dto.Response.Json.ResponseDTO;

@RestControllerAdvice
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ClientErrorApiExceptionHandler {
    
    // 요청 데이터 검증 실패시 응답
    @ExceptionHandler(BindException.class)
    public ResponseEntity<ApiResponse<ResponseDTO>> bindException(BindException e){

        // 400, BAD_REQUEST, [메세지들], null
        ApiResponse<ResponseDTO> apiResponse = ApiResponse.badRequest(
            e.getBindingResult().getAllErrors().stream()
                .map(message -> message.getDefaultMessage())
                .collect(Collectors.toList())
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiResponse);
    }


    @ExceptionHandler(InvalidCsrfTokenException.class)
    public ResponseEntity<ApiResponse<ResponseDTO>> invalidCsrfToken(InvalidCsrfTokenException e) {
        // 403, FORBIDDEN, CSRF 토큰 메세지, null
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(ApiResponse.fordidden("CSRF 토큰 인증에 실패했습니다."));
    }


    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiResponse<ResponseDTO>> handleAccessDenied(AccessDeniedException e) {
        // 403, FORBIDDEN, 세션 메세지, null
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(ApiResponse.fordidden(e.getMessage()));
    }
}
