package assignment.accountbook.member.service;

import assignment.accountbook.exception.member.DoesNotExistMember;
import assignment.accountbook.exception.member.DoesNotMatchPw;
import assignment.accountbook.exception.member.DuplicateEmailException;
import assignment.accountbook.member.dto.*;
import assignment.accountbook.member.entity.Member;
import assignment.accountbook.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    private final PasswordEncoder passwordEncoder;

    @Transactional
    public MemberDTO join(MemberSaveDTO memberSaveDTO) {
        //저장을 위해 Entity 객체로 빌더한다.
        Member member = Member.builder()
                .memberEmail(memberSaveDTO.getMemberEmail())
                .memberPw(passwordEncoder.encode(memberSaveDTO.getMemberPw()))
                .memberRole("ROLE_USER")
                .build();

        try {
            //저장 레포지토리 호출
            Member savedMember = memberRepository.save(member);

            //정상적으로 저장이 되었을 경우 멤버번호와, 이메일을 반환한다.
            return new MemberDTO(savedMember.getMemberId(), savedMember.getMemberEmail());
        } catch (DataAccessException e) {
            //이메일이 중복일 경우 예외를 발생한다.
            throw new DuplicateEmailException("이미 존재하는 이메일 입니다. 입력 Email = " + member.getMemberEmail(), e);
        }
    }
}
