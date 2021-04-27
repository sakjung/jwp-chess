package chess.repository.player;

import chess.domain.feature.Color;

public class Player {
    private final String id;
    private final String password;
    private final long roomId;
    private final String color;

    public Player(final String id, final String password) {
        this(id, password, 0L, Color.NO_COLOR.getColor());
    }

    public Player(final String id, final String password, final long roomId, final String color) {
        this.id = id;
        this.password = password;
        this.roomId = roomId;
        this.color = color;
    }

    public String getId() {
        return id;
    }

    public String getPassword() {
        return password;
    }

    public long getRoomId() {
        return roomId;
    }

    public String getColor() {
        return color;
    }
}
