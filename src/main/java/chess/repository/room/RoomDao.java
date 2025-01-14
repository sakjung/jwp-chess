package chess.repository.room;

import chess.util.JsonConverter;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static chess.repository.DatabaseConnection.closeConnection;
import static chess.repository.DatabaseConnection.getConnection;

public class RoomDao {
    public void addRoom(Room room) throws SQLException {
        String query = "INSERT INTO rooms (name, turn, state) VALUES (?, ?, ?) ON DUPLICATE KEY UPDATE turn = VALUES(turn), state = VALUES(state)";
        Connection con = getConnection();
        PreparedStatement pstmt = con.prepareStatement(query);
        pstmt.setString(1, room.getName());
        pstmt.setString(2, room.getTurn());
        pstmt.setString(3, room.getState().toString());
        pstmt.executeUpdate();
        closeConnection(con);
    }

    public Room findByRoomId(String name) throws SQLException {
        String query = "SELECT * FROM rooms WHERE name = ?";
        Connection con = getConnection();
        PreparedStatement pstmt = con.prepareStatement(query);
        pstmt.setString(1, name);
        ResultSet rs = pstmt.executeQuery();

        Room room = Optional.ofNullable(getRoom(rs))
                .orElseThrow(IllegalArgumentException::new);
        closeConnection(con);
        return room;
    }

    private Room getRoom(ResultSet rs) throws SQLException {
        if (!rs.next()) {
            return null;
        }

        return new Room(
                rs.getString("name"),
                rs.getString("turn"),
                JsonConverter.fromJson(rs.getString("state")));
    }

    public void validateRoomExistence(String name) throws SQLException {
        String query = "SELECT * FROM rooms WHERE name = ?";
        Connection con = getConnection();
        PreparedStatement pstmt = con.prepareStatement(query);
        pstmt.setString(1, name);
        ResultSet rs = pstmt.executeQuery();

        if (rs.next()) {
            throw new IllegalArgumentException();
        }
        closeConnection(con);
    }

    public List<String> getAllRoom() throws SQLException {
        String query = "SELECT name FROM rooms";
        Connection con = getConnection();
        PreparedStatement pstmt = con.prepareStatement(query);
        ResultSet rs = pstmt.executeQuery();

        List<String> rooms = new ArrayList<>();
        while (rs.next()) {
            rooms.add(rs.getString("name"));
        }
        closeConnection(con);
        return rooms;
    }
}
