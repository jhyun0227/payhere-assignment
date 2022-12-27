package assignment.accountbook.util;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Slf4j
@NoArgsConstructor
@Component
public class SecurityUtil {

    public static Optional<String> getCurrentMemberEmail() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null) {
            log.debug("Security Context에 인증 정보가 없습니다.");
            return Optional.empty();
        }

        String memberEmail = null;
        if (authentication.getPrincipal() instanceof UserDetails) {
            UserDetails springSecurityUser = (UserDetails) authentication.getPrincipal();
            memberEmail = springSecurityUser.getUsername();
        } else if (authentication.getPrincipal() instanceof String) {
            memberEmail = (String) authentication.getPrincipal();
        }

        return Optional.ofNullable(memberEmail);
    }

}
