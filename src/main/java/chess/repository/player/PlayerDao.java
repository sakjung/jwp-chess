package chess.repository.player;

import chess.repository.room.InvalidRoomUpdateException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class PlayerDao {
    private JdbcTemplate jdbcTemplate;

    public PlayerDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void enrollPlayer(Player player) {
        String query = "INSERT INTO players (id, password) VALUES (?, ?)";
        jdbcTemplate.update(query, player.getId(), player.getPassword());
    }

    public void allocatePlayer(Player player) {
        try {
            String query = "UPDATE players SET room_id=?, color=? WHERE id=?";
            jdbcTemplate.update(query, player.getRoomId(), player.getColor(), player.getId());
        } catch (Exception e) {
            throw new IllegalArgumentException("그런 유저 없습니다~");
        }

//        String query = "INSERT INTO players (id, password, room_id, color) VALUES (?, ?, ?, ?) ON DUPLICATE KEY UPDATE room_id = VALUES(room_id), color = VALUES(color)";
//        jdbcTemplate.update(query, player.getId(), player.getPassword(), player.getRoomId(), player.getColor());
    }

    public int countPlayers(final long roomId) {
        try {
            String queryForPlayerCount = "SELECT COUNT(*) FROM players WHERE room_id = ?";
            Integer count = jdbcTemplate.queryForObject(queryForPlayerCount, Integer.class, roomId);

            return count;
        } catch (Exception e) {
            return 0;
        }
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

    //    String queryForPlayerId = "SELECT room_id FROM players WHERE id = ?";
//    long playerRoomId = jdbcTemplate.queryForObject(queryForPlayerId, Long.class, playerId);
//
//            if (roomId != playerRoomId && count.equals(2)) {
//        throw new NoVacantRoomException();
//    }

    public boolean isPlayerInRoom(final long roomIdToEnter, final String playerId) {
        String query = "SELECT room_id FROM players WHERE id = ?";
        long currentRoomId = jdbcTemplate.queryForObject(query, Long.class, playerId);

        return currentRoomId == roomIdToEnter;
    }

    public String playerColor(final String playerId) {
        String query = "SELECT color FROM players WHERE id = ?";
        return jdbcTemplate.queryForObject(query, String.class, playerId);
    }

    public Player getPlayer(final String playerId) {
        String query = "SELECT * FROM players WHERE id = ?";
        return jdbcTemplate.queryForObject(query,
                (resultSet, rowNum) -> new Player(
                        resultSet.getString("id"),
                        resultSet.getString("password"),
                        resultSet.getLong("room_id"),
                        resultSet.getString("color")),
                playerId);
    }
}
