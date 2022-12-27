package assignment.accountbook.exception.member;

public class NotAuthentication extends RuntimeException {

    public NotAuthentication(String message) {
        super(message);
    }
}
