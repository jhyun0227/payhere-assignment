package assignment.accountbook.member.service;

import assignment.accountbook.exception.member.DoesNotExistMember;
import assignment.accountbook.member.entity.Member;
import assignment.accountbook.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Component("userDetailsService")
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String memberEmail) throws UsernameNotFoundException {
        return memberRepository.findByEmail(memberEmail)
                .map(member -> createUser(member))
                .orElseThrow(() -> new DoesNotExistMember("존재하지 않는 회원입니다. 입력 Email = " + memberEmail));
    }

    private User createUser(Member member) {
//        List<GrantedAuthority> grantedAuthorities = member.getMemberAuthority().stream()
//                .map(memberAuthority -> new SimpleGrantedAuthority(memberAuthority.getAuthority().getAuthorityName()))
//                .collect(Collectors.toList());

        SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(member.getMemberRole());
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        grantedAuthorities.add(simpleGrantedAuthority);

        return new User(member.getMemberEmail(), member.getMemberPw(), grantedAuthorities);
    }
}
