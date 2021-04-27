package chess.repository.player;

public class InvalidLoginException extends RuntimeException {
    private static final String INVALID_LOGIN_EXCEPTION = "올바른 비밀번호 입력좀.";

    public InvalidLoginException() {
        super(INVALID_LOGIN_EXCEPTION);
    }
}
