package project.spring_basic.service.querys;

import project.spring_basic.data.dto.Response.ModelAttribute.AccountInfoDTO;
import project.spring_basic.data.entity.Member;

public interface MemberServiceQuerys {

    public Member getMember(Long id) throws Exception;

    // 회원 정보 조회 id(문자열) - Member
    public Member getMemberByUserId(String userId) throws Exception;

    // 해당 ID를 가진 회원이 존재하는지 확인
    public boolean memberExistsById (String userId) throws Exception;

    // 해당 ID와 비밀번호를 가진 회원이 존재하는지 확인
    public boolean memberExists(String userId, String password) throws Exception;
    
    // 회원 정보 조회 id(정수) - AccountInfoDTO
    public AccountInfoDTO getAccountInfo(Long id) throws Exception;

}
