package chess.repository.room;

import com.google.gson.JsonObject;

import java.util.Objects;

public class Room {
    private final String name;
    private final String turn;
    private final JsonObject state;

    public Room(final String name, final String turn, final JsonObject state) {
        this.name = name;
        this.turn = turn;
        this.state = state;
    }

    public String getName() {
        return name;
    }

    public String getTurn() {
        return turn;
    }

    public JsonObject getState() {
        return state;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Room room = (Room) o;
        return Objects.equals(name, room.name) && Objects.equals(turn, room.turn) && Objects.equals(state, room.state);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, turn, state);
    }
}
