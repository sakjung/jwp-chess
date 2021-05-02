package chess.controller;

import chess.dto.DeleteDto;
import chess.service.SpringChessService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SpringChessRestController {
    private static final Logger log = LoggerFactory.getLogger("console");
    private static final Logger fileLogger = LoggerFactory.getLogger("file");
    private static final Logger jsonLogger = LoggerFactory.getLogger("json");

    private final SpringChessService springChessService;

    public SpringChessRestController(SpringChessService springChessService) {
        this.springChessService = springChessService;
    }

    @DeleteMapping(value = "/delete/{roomName}")
    public ResponseEntity<DeleteDto> delete(@PathVariable("roomName") String roomName) {
        springChessService.deleteRoom(roomName);
        DeleteDto deleteDto = new DeleteDto(roomName, true);

        return ResponseEntity.ok().body(deleteDto);
    }

    @GetMapping("/file")
    public String logbackFileTest() {
        fileLogger.info("파일로깅 입니다.");
        fileLogger.debug("디버그 파일로깅 입니다.");
        return "파일 로깅 테스트 페이지 입니다";
    }

    @GetMapping("/json")
    public String logbackJsonTest() {
        jsonLogger.info("제이슨 로깅입니다.");
        jsonLogger.debug("디버그 제이슨 로깅입니다.");
        return "제이슨 로깅 테스트 페이지 입니다";
    }
}
