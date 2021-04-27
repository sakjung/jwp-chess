package chess.repository.player;

import chess.repository.room.*;
import chess.util.JsonConverter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PlayerDao {
    private JdbcTemplate jdbcTemplate;

    public PlayerDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void savePlayer(Player player) {
        String query = "INSERT INTO players (id, password, room_id, color) VALUES (?, ?, ?, ?) ON DUPLICATE KEY UPDATE room_id = VALUES(room_id), color = VALUES(color)";
        jdbcTemplate.update(query, player.getId(), player.getPassword(), player.getRoomId(), player.getColor());
    }

    public int countPlayers(final long roomId, final String playerId) {
        String queryForPlayerCount = "SELECT COUNT(*) FROM players WHERE room_id = ?";
        Integer count = jdbcTemplate.queryForObject(queryForPlayerCount, Integer.class, roomId);

        String queryForPlayerId = "SELECT room_id FROM players WHERE id = ?";
        long playerRoomId = jdbcTemplate.queryForObject(queryForPlayerId, Long.class, playerId);

        if (roomId != playerRoomId && count.equals(2)) {
            throw new NoVacantRoomException();
        }
        return count;
    }

    public void validatePlayer(final String playerId, final String password) {
        String query = "SELECT * FROM players WHERE id = ?";

        Player player = jdbcTemplate.queryForObject(
                query,
                (resultSet, rowNum) -> new Player(
                        resultSet.getString("id"),
                        resultSet.getString("password")),
                playerId);

        if (!player.getPassword().equals(password)) {
            throw new InvalidLoginException();
        }
    }

    public void checkPlayerTurn(final long roomId, final String playerId) {
        String queryForGameTurn = "SELECT turn FROM rooms WHERE room_id = ?";
        String actualTurn = jdbcTemplate.queryForObject(queryForGameTurn, String.class, roomId);

        String queryForPlayerTurn = "SELECT color FROM players WHERE id = ?";
        String playerTurn = jdbcTemplate.queryForObject(queryForPlayerTurn, String.class, playerId);

        if (!playerTurn.equals(actualTurn)) {
            throw new IllegalArgumentException(playerId + " 의 턴이 아닙니다.");
        }
    }

    public boolean isPlayerInRoom(final String playerId, final long roomIdToEnter) {
        String query = "SELECT room_id FROM players WHERE id = ?";
        long currentRoomId = jdbcTemplate.queryForObject(query, Long.class, playerId);

        return currentRoomId == roomIdToEnter;
    }

    public String playerColor(final String playerId) {
        String query = "SELECT color FROM players WHERE id = ?";
        return jdbcTemplate.queryForObject(query, String.class, playerId);
    }
}
