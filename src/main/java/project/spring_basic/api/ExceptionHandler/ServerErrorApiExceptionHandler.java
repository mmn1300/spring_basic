package project.spring_basic.api.ExceptionHandler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import project.spring_basic.api.ApiResponse;
import project.spring_basic.data.dto.Response.Json.ResponseDTO;
import project.spring_basic.exception.MemberNotFoundException;
import project.spring_basic.exception.PostNotFoundException;

@RestControllerAdvice
@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class ServerErrorApiExceptionHandler {
    
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiResponse<ResponseDTO>> handleRuntimeException(RuntimeException e){

        // 500, INTERNAL_SERVER_ERROR, [메세지들], null
        ApiResponse<ResponseDTO> apiResponse = ApiResponse.internalServerError(e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiResponse);
    }


    @ExceptionHandler(PostNotFoundException.class)
    public ResponseEntity<ApiResponse<ResponseDTO>> handlePostException(PostNotFoundException e){

        // 500, INTERNAL_SERVER_ERROR, [메세지들], null
        ApiResponse<ResponseDTO> apiResponse = ApiResponse.internalServerError(e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiResponse);
    }


    @ExceptionHandler(MemberNotFoundException.class)
    public ResponseEntity<ApiResponse<ResponseDTO>> handleMemberException(MemberNotFoundException e){

        // 500, INTERNAL_SERVER_ERROR, [메세지들], null
        ApiResponse<ResponseDTO> apiResponse = ApiResponse.internalServerError(e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiResponse);
    }


    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse<ResponseDTO>> handleArgumentException(IllegalArgumentException e){

        // 500, INTERNAL_SERVER_ERROR, [메세지들], null
        ApiResponse<ResponseDTO> apiResponse = ApiResponse.internalServerError(e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiResponse);
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<ResponseDTO>> handleGenericException(Exception e) {

        // 500, INTERNAL_SERVER_ERROR, [메세지들], null
        ApiResponse<ResponseDTO> apiResponse = ApiResponse.internalServerError(e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiResponse);
    }
}
