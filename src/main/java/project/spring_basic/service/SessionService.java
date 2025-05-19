package project.spring_basic.service;

import jakarta.servlet.http.HttpSession;
import project.spring_basic.data.dto.Request.NewAccountDTO;
import project.spring_basic.data.dto.Response.Json.UserInfoDTO;
import project.spring_basic.data.dto.Response.ModelAttribute.OptionDTO;
import project.spring_basic.data.entity.Member;

public interface SessionService {

    // 세션 생성
    public void createSession(HttpSession session, Member member) throws Exception;

    // 모든 세션 제거
    public void deleteAllSession(HttpSession session) throws Exception;

    // 유저 정보 UserInfoDTO 인스턴스에 담아 반환
    public UserInfoDTO getUserInfo(UserInfoDTO dto, HttpSession session) throws Exception;

    // 유저 id 반환
    public Long getId(HttpSession session);

    // 유저 문자열 id 반환
    public String getUserId(HttpSession session);

    // 유저 닉네임 반환
    public String getNickname(HttpSession session);

    // 세션 존재 여부에 따른 템플릿 리턴
    public String getTemplateOrDefault(HttpSession session, String view);

    // 세션 정보 갱신
    public void updateSession(HttpSession session, NewAccountDTO newAccountDTO) throws Exception;

    // 세션 정보 기반 게시판 옵션 정보 반환
    public OptionDTO getUserOptions(HttpSession session, Long id);
}