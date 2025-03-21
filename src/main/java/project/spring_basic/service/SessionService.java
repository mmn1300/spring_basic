package project.spring_basic.service;

import jakarta.servlet.http.HttpSession;
import project.spring_basic.data.dto.Response.UserInfoDTO;
import project.spring_basic.data.entity.Member;

public interface SessionService {

    // 세션 생성
    public void createSessionfromUserId(HttpSession session, Member member) throws Exception;

    // 모든 세션 제거
    public void deleteAllSession(HttpSession session) throws Exception;

    // 유저 정보 UserInfoDTO 인스턴스에 담아 반환
    public UserInfoDTO getUserInfo(UserInfoDTO dto, HttpSession session) throws Exception;

    // 유저 문자열 id 반환
    public String getUserId(HttpSession session);

    // 유저 닉네임 반환
    public String getNickname(HttpSession session);

    // 세션 존재 여부에 따른 템플릿 리턴
    public String getTemplateOrDefault(HttpSession session, String view);
}