package assignment.accountbook.member.entity;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Member {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long memberId;

    @Column(name = "member_email", unique = true, nullable = false, length = 30)
    private String memberEmail;

    @Column(name = "member_pw", nullable = false, length = 300)
    private String memberPw;

    @Column(name = "member_role", nullable = false)
    private String memberRole;

}
