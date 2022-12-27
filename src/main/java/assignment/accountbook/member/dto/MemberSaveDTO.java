package assignment.accountbook.member.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemberSaveDTO {

    @NotBlank(message = "이메일은 필수로 입력해야합니다.")
    @Email(message = "이메일 형식으로 입력해주세요.")
    private String memberEmail;

    @NotBlank(message = "비밀번호는 필수로 입력해야합니다.")
    private String memberPw;

}
