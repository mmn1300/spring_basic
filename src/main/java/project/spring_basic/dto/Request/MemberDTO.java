package project.spring_basic.dto.Request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class MemberDTO {
    @Size(min=8, max=15)
    @NotNull private String userId;

    @Size(min=8, max=15)
    @NotNull private String pw;

    @Size(min=1, max=45)
    @NotNull private String name;

    @Size(min=3, max=80)
    @NotNull private String email;

    @Size(min=13, max=13)
    @NotNull private String phone;
}
