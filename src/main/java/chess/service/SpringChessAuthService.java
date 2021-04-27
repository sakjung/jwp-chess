package chess.service;

import chess.domain.feature.Color;
import chess.repository.player.Player;
import chess.repository.player.PlayerDao;
import org.springframework.stereotype.Service;

@Service
public class SpringChessAuthService {
    private final PlayerDao playerDao;

    public SpringChessAuthService(final PlayerDao playerDao) {
        this.playerDao = playerDao;
    }

    public void validatePlayer(final String playerId, final String password) {
        playerDao.validatePlayer(playerId, password);
    }

    public String loginPlayer(final long roomId, final String playerId, final String password) {
        int currentPlayerCount = playerDao.countPlayers(roomId, playerId);

        if (currentPlayerCount == 2) {
            return playerDao.playerColor(playerId);
        }
        String colorAssigned = Color.assignColor(currentPlayerCount);
        Player player = new Player(playerId, password, roomId, colorAssigned);
        playerDao.savePlayer(player);

        return colorAssigned;
    }

    public void checkPlayerTurn(final long roomId, final String playerId) {
        playerDao.checkPlayerTurn(roomId, playerId);
    }
}
