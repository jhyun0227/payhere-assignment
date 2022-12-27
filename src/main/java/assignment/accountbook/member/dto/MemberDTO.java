package assignment.accountbook.member.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemberDTO {

    private Long memberId;
    private String memberEmail;
    private String memberPw;

    public MemberDTO(Long memberId, String memberEmail) {
        this.memberId = memberId;
        this.memberEmail = memberEmail;
    }
}
