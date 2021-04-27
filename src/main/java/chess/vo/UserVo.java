package chess.vo;

public class UserVo {
    private final String playerId;
    private final String password;

    public UserVo(final String playerId, final String password) {
        this.playerId = playerId;
        this.password = password;
    }

    public String getPlayerId() {
        return playerId;
    }

    public String getPassword() {
        return password;
    }
}
