package project.spring_basic.service;

import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpSession;
import project.spring_basic.dto.Response.UserInfoDTO;
import project.spring_basic.entity.Member;

@Service
public class SessionService {

    public void createSessionfromUserId(HttpSession session, Member member) throws Exception{
        Long id = (Long) session.getAttribute("id");
        if(id == null){
            session.setAttribute("id", member.getId());
            session.setAttribute("userId", member.getUserId());
            session.setAttribute("nickname", member.getNickname());
        }
    }
    
    public void deleteAllSession(HttpSession session) throws Exception{
        Long id = (Long) session.getAttribute("id");
        if(id != null){
            session.removeAttribute("id");
            session.removeAttribute("userId");
            session.removeAttribute("nickname");
        }
    }

    public UserInfoDTO getUserInfo(UserInfoDTO dto, HttpSession session) throws Exception{
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

    // 세션 존재 여부에 따른 템플릿 리턴
    public String getTemplateOrDefault(HttpSession session, String view) {
        Long id = (Long) session.getAttribute("id");
        if(id != null){
            return view;
        }else{
            return "redirect:/login";
        }
    }
}
