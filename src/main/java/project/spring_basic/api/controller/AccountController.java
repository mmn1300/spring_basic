package project.spring_basic.api.controller;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.servlet.http.HttpSession;


public interface AccountController {


    @Operation(
        summary = "마이페이지 렌더링",
        description = """
            세션에서 사용자 ID를 가져와 해당 회원 정보를 조회하고,
            성공 시 my_page를 렌더링, 실패시 login_page를 렌더링 함
            예외 발생 시 error로 리다이렉트 함
        """,
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "회원 정보 조회 성공"
            ),
            @ApiResponse(
                responseCode = "303",
                description = "세션 미존재. login 템플릿으로 리다이렉트"
            ),
            @ApiResponse(
                responseCode = "401",
                description = "인증 정보 미존재로 인한 접근 차단"
            ),
            @ApiResponse(
                responseCode = "500",
                description = "서버 오류로 인한 실패. error 템플릿으로 리다이렉트"
            )
        }
    )
    public ModelAndView info(HttpSession session, RedirectAttributes redirectAttributes);
}
