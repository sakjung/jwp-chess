package chess.repository.player;

public class NoVacantRoomException extends RuntimeException {
    private static final String NO_VACANT_ROOM_ERROR = "방 인원이 이미 가득 찼습니다.";

    public NoVacantRoomException() {
        super(NO_VACANT_ROOM_ERROR);
    }
}
