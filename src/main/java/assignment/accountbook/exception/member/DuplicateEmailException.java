package assignment.accountbook.exception.member;

public class DuplicateEmailException extends RuntimeException {

    public DuplicateEmailException(String message, Throwable cause) {
        super(message, cause);
    }

}
