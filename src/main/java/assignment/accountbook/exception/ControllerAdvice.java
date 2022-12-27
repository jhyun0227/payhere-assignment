package assignment.accountbook.exception;

import assignment.accountbook.exception.consumption.DoesNotExitsConsumption;
import assignment.accountbook.exception.member.DoesNotExistMember;
import assignment.accountbook.exception.member.DoesNotMatchPw;
import assignment.accountbook.exception.member.DuplicateEmailException;
import assignment.accountbook.exception.member.NotAuthentication;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class ControllerAdvice {

    /**
     * Validation 예외 처리
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> argumentNotValid(MethodArgumentNotValidException e) {
        log.error("##### MethodArgumentNotValidException 발생 #####");

        Map<String, String> errors = new HashMap<>();
        e.getBindingResult().getAllErrors()
                .forEach(c -> errors.put(((FieldError) c).getField(), c.getDefaultMessage()));

        return ResponseEntity.badRequest().body(errors);
    }

    /**
     * 회원가입 이메일 중복 예외 처리
     */
    @ExceptionHandler(DuplicateEmailException.class)
    public ResponseEntity<Map<String, String>> duplicateEmailExHandler(DuplicateEmailException e) {
        log.error("##### DupicateEmailException 발생 #####");

        Map<String, String> error = new HashMap<>();
        error.put("message", e.getMessage());

        return ResponseEntity.badRequest().body(error);
    }

    /**
     * 로그인 시, 회원이 존재하지 않을 경우 예외 처리
     */
    @ExceptionHandler(DoesNotExistMember.class)
    public ResponseEntity<Map<String, String>> doesNotExistMember(DoesNotExistMember e) {
        log.error("##### DoesNotExistMember 발생 #####");

        Map<String, String> error = new HashMap<>();
        error.put("message", e.getMessage());

        return ResponseEntity.badRequest().body(error);
    }

    /**
     * 로그인 시, 비밀번호가 일치하지 않을 경우 예외 처리
     */
    @ExceptionHandler(DoesNotMatchPw.class)
    public ResponseEntity<Map<String, String>> doesNotMatchPw(DoesNotMatchPw e) {
        log.error("##### DoesNotMatchPw 발생 #####");

        Map<String, String> error = new HashMap<>();
        error.put("message", e.getMessage());

        return ResponseEntity.badRequest().body(error);
    }

    /**
     * Security Context에서 member정보 가져올 때 ID값이 없을 경우
     */
    @ExceptionHandler(NotAuthentication.class)
    public ResponseEntity<Map<String, String>> notAuthentication(NotAuthentication e) {
        log.error("##### NotAuthentication 발생 #####");

        Map<String, String> error = new HashMap<>();
        error.put("message", e.getMessage());

        return ResponseEntity.badRequest().body(error);
    }

    /**
     * 존재하지 않는, 소비내역ID 일 경우 예외처리
     */
    @ExceptionHandler(DoesNotExitsConsumption.class)
    public ResponseEntity<Map<String, String>> doesNotExistConsumption(DoesNotExitsConsumption e) {
        log.error("##### DoesNotExitsConsumption 발생 #####");

        Map<String, String> error = new HashMap<>();
        error.put("message", e.getMessage());

        return ResponseEntity.badRequest().body(error);
    }
    
}
