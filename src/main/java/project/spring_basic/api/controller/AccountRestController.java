package project.spring_basic.api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.RequestBody;

import project.spring_basic.api.ApiResponse;
import project.spring_basic.api.swaggerDto.SwaggerBooleanDTO;
import project.spring_basic.api.swaggerDto.SwaggerErrorDTO;
import project.spring_basic.api.swaggerDto.SwaggerNullDTO;
import project.spring_basic.api.swaggerDto.SwaggerResponseDTO;
import project.spring_basic.data.dto.Request.AccountDTO;
import project.spring_basic.data.dto.Request.MemberDTO;
import project.spring_basic.data.dto.Request.NewAccountDTO;
import project.spring_basic.data.dto.Response.Json.ResponseDTO;


public interface AccountRestController {


    @Operation(
        summary = "아이디 검사",
        description = "파라미터로 받은 문자열 id 값을 기반으로 해당 유저가 데이터베이스에 존재하는지를 응답함",
        parameters = {
            @Parameter(
                name = "id",
                description = "사용자의 문자열 아이디 값",
                required = true,
                in = ParameterIn.PATH
            )
        },
        responses = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "200",
                description = "문자열 id 존재 여부 조회 성공",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = SwaggerBooleanDTO.class)
                )
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "500",
                description = "서버 오류로 인한 실패",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = SwaggerErrorDTO.class)
                )
            ),
        }
    )
    public ResponseEntity<ApiResponse<ResponseDTO>> checkId(@PathVariable("id") String userId) throws Exception;



    @Operation(
        summary = "아이디 비밀번호 일치 검사",
        description = "body로 받은 id값과 비밀번호를 가진 유저가 데이터베이스에 존재하는지를 응답함",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "문자열 id 값과 비밀번호에 해당하는 회원이 존재하는지를 응답함",
            required = true,
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = AccountDTO.class)
            )
        ),
        responses = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "200",
                description = "회원 존재 여부 조회 성공",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = SwaggerBooleanDTO.class)
                )
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "400",
                description = "요청 데이터 형식 미준수",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = SwaggerNullDTO.class)
                )
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "403",
                description = "CSRF 토큰 미존재로 인한 요청 처리 거부",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = SwaggerErrorDTO.class)
                )
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "500",
                description = "서버 오류로 인한 실패",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = SwaggerErrorDTO.class)
                )
            ),
        }
    )
    public ResponseEntity<ApiResponse<ResponseDTO>> checkAccount(@Valid @RequestBody AccountDTO accountDTO) throws Exception;



    @Operation(
        summary = "계정 생성",
        description = "body로 받은 값들을 기반으로 데이터베이스에 회원 정보를 생성함",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "입력된 정보를 기반으로 데이터베이스에 회원을 생성함",
            required = true,
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = MemberDTO.class)
            )
        ),
        responses = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "200",
                description = "회원 생성 성공",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = SwaggerResponseDTO.class)
                )
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "400",
                description = "요청 데이터 형식 미준수",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = SwaggerNullDTO.class)
                )
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "403",
                description = "CSRF 토큰 미존재로 인한 요청 처리 거부",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = SwaggerErrorDTO.class)
                )
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "500",
                description = "서버 오류로 인한 실패",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = SwaggerErrorDTO.class)
                )
            ),
        }
    )
    public ResponseEntity<ApiResponse<ResponseDTO>> setMember(@Valid @RequestBody MemberDTO memberDTO) throws Exception;



    @Operation(
        summary = "계정 정보 수정",
        description = "body로 받은 값들을 기반으로 데이터베이스에 존재하는 회원 정보를 수정함",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "입력된 정보를 기반으로 데이터베이스에 존재하는 회원 정보를 수정함",
            required = true,
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = NewAccountDTO.class)
            )
        ),
        responses = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "200",
                description = "회원 정보 수정 성공",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = SwaggerResponseDTO.class)
                )
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "400",
                description = "요청 데이터 형식 미준수",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = SwaggerNullDTO.class)
                )
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "401",
                description = "인증 정보 미존재로 인한 접근 차단",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = SwaggerErrorDTO.class)
                )
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "403",
                description = "CSRF 토큰 미존재로 인한 요청 처리 거부",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = SwaggerErrorDTO.class)
                )
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "500",
                description = "서버 오류로 인한 실패",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = SwaggerErrorDTO.class)
                )
            ),
        }
    )
    public ResponseEntity<ApiResponse<ResponseDTO>> updateAccount(@Valid @RequestBody NewAccountDTO newAccountDTO, HttpSession session) throws Exception;
}
