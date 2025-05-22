package project.spring_basic.service.commands;

import project.spring_basic.data.dto.Request.MemberDTO;
import project.spring_basic.data.dto.Request.NewAccountDTO;

public interface MemberServiceCommands {

    // 회원 정보 DB 삽입
    public void save(MemberDTO memberDTO) throws Exception;

    // 계정 정보 수정
    public void update(NewAccountDTO newAccountDTO, Long id) throws Exception;
}
