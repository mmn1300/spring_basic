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
public class AccountDTO {
    @Size(min=8, max=15, message="아이디의 길이는 8자 이상 15자 이하여야 합니다.")
    @NotNull(message="아이디가 존재해야 합니다.")
    private String id;

    @Size(min=8, max=15, message="비밀번호의 길이는 8자 이상 15자 이하여야 합니다.")
    @NotNull(message="비밀번호가 존재해야 합니다.")
    private String pw;
}
