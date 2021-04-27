package chess.controller;

import chess.dto.DeleteDto;
import chess.repository.room.DuplicateRoomNameException;
import chess.repository.room.InvalidRoomDeleteException;
import chess.repository.room.NoSuchRoomNameException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class SpringChessControllerAdvice {
    @ExceptionHandler(DuplicateRoomNameException.class)
    public ModelAndView gameHandleError(HttpServletRequest req) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("alert", req.getParameter("roomName") + "는 이미 존재하는 방입니다.");
        modelAndView.setViewName("index");
        modelAndView.setStatus(HttpStatus.CONFLICT);
        return modelAndView;
    }

    @ExceptionHandler(NoSuchRoomNameException.class)
    public String loadHandleError() {
        return "repository";
    }

    @ExceptionHandler(InvalidRoomDeleteException.class)
    public ResponseEntity<DeleteDto> deleteHandleError(HttpServletRequest req) {
        String roomName = req.getServletPath();
        DeleteDto deleteDto = new DeleteDto(roomName, false);
        return ResponseEntity.badRequest().body(deleteDto);
    }
}
