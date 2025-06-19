package project.spring_basic.api.custom.CustomHandler;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import project.spring_basic.api.ApiResponse;
import project.spring_basic.data.dto.Response.Json.ResponseDTO;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {
    
    @Autowired
    private ObjectMapper objectMapper;


    // CSRF 토큰이 존재하지 않는 요청 처리 -> 403 에러 응답
    @Override
    public void handle(HttpServletRequest request,
                       HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException {

        ApiResponse<ResponseDTO> errorResponse = ApiResponse.fordidden(
            "접근이 거부되었습니다. (CSRF 토큰 오류)"
        );

        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType("application/json;charset=UTF-8");
        objectMapper.writeValue(response.getWriter(), errorResponse);
    }
}
