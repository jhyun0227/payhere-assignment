package assignment.accountbook.member.service;

import assignment.accountbook.exception.member.DoesNotExistMember;
import assignment.accountbook.exception.member.DoesNotMatchPw;
import assignment.accountbook.exception.member.DuplicateEmailException;
import assignment.accountbook.member.dto.MemberDTO;
import assignment.accountbook.member.dto.MemberLoginDTO;
import assignment.accountbook.member.dto.MemberSaveDTO;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*;

@Transactional
@SpringBootTest
class MemberServiceTest {

    @Autowired
    MemberService memberService;

    @Test
    void 회원가입() {
        // 회원가입이 정상적으로 되었을 경우

        // 1-1.회원저장
        MemberSaveDTO saveDTO1 = new MemberSaveDTO("12345@12345.com", "12345");

        // 1-2.정상적으로 회원가입이 되었을 경우 MemberDTO를 반환한다.
        assertThat(memberService.join(saveDTO1)).isInstanceOf(MemberDTO.class);

        //=====================================================================================//

        // 중복된 이메일로 가입을 하였을 경우
        MemberSaveDTO saveDTO2 = new MemberSaveDTO("12345@12345.com", "12345");

        // 2-1. 이메일 중복 예외를 발생한다.
        assertThatThrownBy(() -> memberService.join(saveDTO2))
                .isInstanceOf(DuplicateEmailException.class);
    }
}