package project.spring_basic.service;

import project.spring_basic.data.dto.Request.MemberDTO;
import project.spring_basic.data.dto.Request.NewAccountDTO;
import project.spring_basic.data.dto.Response.ModelAttribute.AccountInfoDTO;
import project.spring_basic.data.entity.Member;

public interface MemberService {

    // 해당 ID를 가진 회원이 존재하는지 확인
    public boolean memberExistsById (String userId) throws Exception;

    // 해당 ID와 비밀번호를 가진 회원이 존재하는지 확인
    public boolean memberExists(String userId, String password) throws Exception;

    // 회원 정보 조회 id(문자열) - Member
    public Member getMemberByUserId(String userId) throws Exception;

    // 회원 정보 조회 id(정수) - AccountInfoDTO
    public AccountInfoDTO getAccountInfo(Long id) throws Exception;



    // 회원 정보 DB 삽입
    public void save(MemberDTO memberDTO) throws Exception;

    // 계정 정보 수정
    public void update(NewAccountDTO newAccountDTO, Long id) throws Exception;
}