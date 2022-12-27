package assignment.accountbook.member.controller;

import assignment.accountbook.jwt.JwtFilter;
import assignment.accountbook.jwt.TokenDTO;
import assignment.accountbook.jwt.TokenProvider;
import assignment.accountbook.member.dto.*;
import assignment.accountbook.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;

    //jwt토큰
    private final TokenProvider tokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    /**
     * 회원가입
     */
    @PostMapping("/join")
    public ResponseEntity<Map<String, String>> join(@Validated @RequestBody MemberSaveDTO memberSaveDTO) {
        MemberDTO joinMember = memberService.join(memberSaveDTO);

        Map<String, String> result = new HashMap<>();
        result.put("message", "정상적으로 회원가입 되었습니다.");
        result.put("memberId", String.valueOf(joinMember.getMemberId()));
        result.put("memberEmail", joinMember.getMemberEmail());

        return ResponseEntity.ok().body(result);
    }

    /**
     * 로그인
     */
    @GetMapping("/login")
    public ResponseEntity<TokenDTO> login(@Validated @RequestBody MemberLoginDTO memberLoginDTO) {

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                memberLoginDTO.getMemberEmail(),
                memberLoginDTO.getMemberPw()
        );

        Authentication authenticate = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authenticate);

        String jwt = tokenProvider.createToken(authenticate);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(JwtFilter.AUTHORIZATION_HEADER, "Bearer " + jwt);

        TokenDTO tokenDTO = new TokenDTO(jwt);

        return new ResponseEntity<>(tokenDTO, httpHeaders, HttpStatus.OK);
    }

    /**
     * 로그아웃
     * 구현하지 못했습니다.
     */
    @GetMapping("/logout")
    public ResponseEntity<Map<String, String>> logout() {
        //Security Context 초기화
        SecurityContextHolder.clearContext();

        Map<String, String> result = new HashMap<>();
        result.put("message", "정상적으로 로그아웃 되었습니다.");

        return ResponseEntity.ok().body(result);
    }

}
