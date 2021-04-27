package chess.service;

import chess.domain.feature.Color;
import chess.repository.player.NoVacantRoomException;
import chess.repository.player.Player;
import chess.repository.player.PlayerDao;
import org.springframework.stereotype.Service;

@Service
public class SpringChessAuthService {
    private final PlayerDao playerDao;

    public SpringChessAuthService(final PlayerDao playerDao) {
        this.playerDao = playerDao;
    }


    public String assignPlayerColor(final long roomId, final String playerId) {
        Player player = playerDao.getPlayer(playerId);

        if (player.getRoomId() == roomId) {
            return player.getColor();
        }

        int currentPlayerCount = playerDao.countPlayers(roomId);
        if (currentPlayerCount == 2) {
            throw new NoVacantRoomException();
        }

        player.setRoomId(roomId);
        String colorAssigned = Color.assignColor(currentPlayerCount);
        player.setColor(colorAssigned);
        playerDao.allocatePlayer(player);

        return colorAssigned;
    }

    public void checkPlayerTurn(final long roomId, final String playerId) {
        playerDao.checkPlayerTurn(roomId, playerId);
    }

    public void login(final String playerId, final String password) {
        Player player = new Player(playerId, password);
        playerDao.enrollPlayer(player);
    }
}
