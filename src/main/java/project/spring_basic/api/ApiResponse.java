package project.spring_basic.api;

import java.util.List;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import project.spring_basic.data.dto.Response.Json.ErrorDTO;
import project.spring_basic.data.dto.Response.Json.ResponseDTO;

@Getter
public class ApiResponse<T> {
    private int code;
    private HttpStatus status;
    private List<String> messages;
    private T data;


    public ApiResponse(HttpStatus status, List<String> messages, T data){
        this.code = status.value();
        this.status = status;
        this.messages = messages;
        this.data = data;
    }


    // 외부 호출용 인스턴스 생성 제네릭 메서드
    private static <T> ApiResponse<T> of(HttpStatus status, List<String> messages, T data){
        return new ApiResponse<T>(status, messages, data);
    }

    // 데이터가 존재하지 않는 응답용 메서드
    private static <T> ApiResponse<T> of(HttpStatus status, List<String> messages){
        return new ApiResponse<T>(status, messages, null);
    }

    // 메시지가 존재하지 않는 응답용 메서드
    private static <T> ApiResponse<T> of(HttpStatus status, T data){
        return of(status, null, data);
    }



    // 200 OK 인 응답용 메서드
    public static <T> ApiResponse<T> ok(T data){
        return of(HttpStatus.OK, data);
    }

    // 400 Bad Request 인 응답용 메서드
    public static <T> ApiResponse<T> badRequest(List<String> messages){
        return of(HttpStatus.BAD_REQUEST, messages);
    }

    // 403 Forbidden 인 응답용 메서드
    public static <T> ApiResponse<T> fordidden(T data){
        return of(HttpStatus.FORBIDDEN, data);
    }

    public static <T> ApiResponse<ResponseDTO> fordidden(String errorMessage){
        ErrorDTO errorDTO = new ErrorDTO(false, errorMessage);
        return fordidden(errorDTO);
    }

    // 500 Internal Server Error 인 응답용 메서드
    public static <T> ApiResponse<T> internalServerError(T data){
        return of(HttpStatus.INTERNAL_SERVER_ERROR, data);
    }

    public static <T> ApiResponse<ResponseDTO> internalServerError(String errorMessage){
        ErrorDTO errorDTO = new ErrorDTO(false, errorMessage);
        return internalServerError(errorDTO);
    }
}
