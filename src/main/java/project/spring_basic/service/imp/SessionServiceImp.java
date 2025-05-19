package project.spring_basic.service.imp;

import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpSession;
import project.spring_basic.data.dto.Request.NewAccountDTO;
import project.spring_basic.data.dto.Response.Json.UserInfoDTO;
import project.spring_basic.data.dto.Response.ModelAttribute.OptionDTO;
import project.spring_basic.data.entity.Member;
import project.spring_basic.exception.DtoNullException;
import project.spring_basic.service.SessionService;

@Service
public class SessionServiceImp implements SessionService {
    
    // 세션 생성
    public void createSession(HttpSession session, Member member) throws Exception {
        Long id = (Long) session.getAttribute("id");
        if(id == null){
            session.setAttribute("id", member.getId());
            session.setAttribute("userId", member.getUserId());
            session.setAttribute("nickname", member.getNickname());
        }
    }
    
    // 모든 세션 제거
    public void deleteAllSession(HttpSession session) throws Exception {
        Long id = (Long) session.getAttribute("id");
        if(id != null){
            session.removeAttribute("id");
            session.removeAttribute("userId");
            session.removeAttribute("nickname");
        }
    }

    // 유저 정보 UserInfoDTO 인스턴스에 담아 반환
    public UserInfoDTO getUserInfo(UserInfoDTO dto, HttpSession session) throws Exception {
        String userId = (String) session.getAttribute("userId");
        String nickname = (String) session.getAttribute("nickname");

        if(userId != null){
            dto.setMessage(true);
            dto.setId(userId);
            dto.setNickname(nickname);
        }else{
            dto.setMessage(false);
        }
        return dto;
    }

    // 유저 id 반환
    public Long getId(HttpSession session) {
        Long id = (Long) session.getAttribute("id");
        if(id != null){
            return id;
        }else{
            return -1L;
        }
    }

    // 유저 문자열 id 반환
    public String getUserId(HttpSession session) {
        String userId = (String) session.getAttribute("userId");
        if(userId != null){
            return userId;
        }else{
            return "";
        }
    }

    // 유저 닉네임 반환
    public String getNickname(HttpSession session) {
        String nickname = (String) session.getAttribute("nickname");
        if(nickname != null){
            return nickname;
        }else{
            return "";
        }
    }

    // 세션 존재 여부에 따른 템플릿 리턴
    public String getTemplateOrDefault(HttpSession session, String view) {
        Long id = (Long) session.getAttribute("id");
        if(id != null){
            return view;
        }else{
            return "redirect:/login";
        }
    }

    // 세션 정보 갱신
    public void updateSession(HttpSession session, NewAccountDTO newAccountDTO) throws Exception {
        if (newAccountDTO == null){
            throw new DtoNullException("DTO가 존재하지 않습니다.");
        }
        session.setAttribute("userId", newAccountDTO.getUserId());
        session.setAttribute("nickname", newAccountDTO.getNickname());
    }

    // 세션 정보 기반 게시판 옵션 정보 반환
    public OptionDTO getUserOptions(HttpSession session, Long id){
        Long sessionId = (Long) session.getAttribute("id");
        String userId = (String) session.getAttribute("userId");
        if(id == sessionId){
            return new OptionDTO(userId, id);
        }else{
            return new OptionDTO("", null);
        }
    }
}
