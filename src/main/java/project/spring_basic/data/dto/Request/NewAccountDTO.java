package project.spring_basic.data.dto.Request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class NewAccountDTO {
    @Size(min=8, max=15, message="아이디의 길이는 8자 이상 15자 이하여야 합니다.")
    @NotNull(message="아이디가 존재해야 합니다.")
    private String userId;

    @Size(min=1, max=45, message="닉네임의 길이는 1자 이상 45자 이하여야 합니다.")
    @NotNull(message="닉네임이 존재해야 합니다.")
    private String nickname;

    @Size(min=3, max=80, message="이메일의 길이는 3자 이상 80자 이하여야 합니다.")
    @NotNull(message="이메일이 존재해야 합니다.")
    private String email;

    @Size(min=13, max=13, message="휴대전화 번호는 13자여야 합니다.")
    @NotNull(message="휴대전화 번호가 존재해야 합니다.")
    private String phone;
}
