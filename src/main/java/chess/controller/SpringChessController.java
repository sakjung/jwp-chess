package chess.controller;

import chess.domain.board.Position;
import chess.domain.feature.Color;
import chess.domain.game.ChessGame;
import chess.domain.game.Result;
import chess.domain.piece.Piece;
import chess.dto.OutcomeDto;
import chess.dto.PieceDto;
import chess.dto.ScoreDto;
import chess.dto.TurnDto;
import chess.repository.room.Room;
import chess.service.SpringChessAuthService;
import chess.service.SpringChessService;
import chess.vo.UserVo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.RequestContextUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

@Controller
public class SpringChessController {
    private final SpringChessService springChessService;
    private final SpringChessAuthService springChessAuthService;

    public SpringChessController(SpringChessService springChessService, SpringChessAuthService springChessAuthService) {
        this.springChessService = springChessService;
        this.springChessAuthService = springChessAuthService;
    }

    @GetMapping("/")
    public String index(HttpServletRequest request) {
        Map<String, ?> inputFlashMap = RequestContextUtils.getInputFlashMap(request);
        if (inputFlashMap != null) {
            UserVo user = (UserVo) inputFlashMap.get("USER");
            request.getSession().setAttribute("USER", user);
        }

        return "index";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @PostMapping("/login")
    public String login(HttpServletRequest request, RedirectAttributes redirectAttributes) {
        Map<String, String[]> parameterMap = request.getParameterMap();

        String playerId = parameterMap.get("playerId")[0];
        String password = parameterMap.get("password")[0];

        redirectAttributes.addFlashAttribute("USER", new UserVo(playerId, password));

        return "redirect:/";
    }

    @PostMapping("/game")
    public String game(@RequestParam("roomName") String roomName, HttpSession session) {
        long id = springChessService.initializeRoom(roomName);

        UserVo user = (UserVo) session.getAttribute("USER");
        String playerId = user.getPlayerId();
        String password = user.getPassword();

        String colorAssigned = springChessAuthService.loginPlayer(id, playerId, password);

        return "redirect:/game/" + id + "/" + colorAssigned;
    }

    @PostMapping("/load")
    public String load(@RequestParam("roomName") String roomName, HttpSession session) {
        long id = springChessService.getRoomId(roomName);

        UserVo user = (UserVo) session.getAttribute("USER");
        String playerId = user.getPlayerId();
        String password = user.getPassword();

        String colorAssigned = springChessAuthService.loginPlayer(id, playerId, password);

        return "redirect:/game/" + id + "/" + colorAssigned;
    }

    @GetMapping("/game/{id}/*")
    public ModelAndView enterRoom(@PathVariable("id") long id) {
        final Room room = springChessService.loadRoom(id);
        final ChessGame chessGame = springChessService.createChessGame(room);
        final String roomName = room.getName();

        ModelAndView modelAndView = new ModelAndView();
        addChessGame(modelAndView, chessGame);
        modelAndView.setViewName("game");
        modelAndView.addObject("roomName", roomName);

        return modelAndView;
    }
//    @SessionAttribute UserVo user
    @PostMapping(value = "/game/{id}/*/move", consumes = "text/plain")
    public ModelAndView move(HttpSession httpSession, @PathVariable("id") long id, @RequestBody String moveCommand) {
        UserVo user = (UserVo) httpSession.getAttribute("USER");
        String playerId = user.getPlayerId();
        springChessAuthService.checkPlayerTurn(id, playerId);
        ChessGame chessGame = springChessService.movePiece(id, moveCommand);

        ModelAndView modelAndView = new ModelAndView();
        addChessGame(modelAndView, chessGame);
        modelAndView.setViewName("game");

        return modelAndView;
    }

    @GetMapping(value = "/rooms")
    public ModelAndView rooms() {
        List<String> roomNames = springChessService.getAllSavedRooms();

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("roomNames", roomNames);
        modelAndView.setViewName("repository");

        return modelAndView;
    }

    private void addChessGame(ModelAndView modelAndView, ChessGame chessGame) {
        Color turn = chessGame.getTurn();
        modelAndView.addObject("turn", new TurnDto(turn));

        Map<Position, Piece> chessBoard = chessGame.getChessBoardAsMap();
        for (Map.Entry<Position, Piece> entry : chessBoard.entrySet()) {
            modelAndView.addObject(entry.getKey().getPosition(), new PieceDto(entry.getValue()));
        }

        Result result = chessGame.calculateResult();
        modelAndView.addObject("score", new ScoreDto(result));
        if (!chessGame.isOngoing()) {
            modelAndView.addObject("outcome", new OutcomeDto(result));
        }
    }
}
