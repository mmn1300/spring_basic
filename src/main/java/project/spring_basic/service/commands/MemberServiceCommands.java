package project.spring_basic.service.commands;

import project.spring_basic.data.dto.Request.NewAccountDTO;
import project.spring_basic.data.entity.Member;

public interface MemberServiceCommands {

    // 회원 정보 DB 삽입
    public void save(Member member) throws Exception;

    // 계정 정보 수정
    public void update(NewAccountDTO newAccountDTO, Member member) throws Exception;
}
