import chess.controller.SpringChessController;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoggingTest {
    private static final Logger log = LoggerFactory.getLogger(SpringChessController.class);
    private static final Logger fileLogger = LoggerFactory.getLogger("file");

    @Test
    void loggingLearningTest() {
        log.error("An ERROR Message");
        fileLogger.info("파일 로깅 입니다.");
    }
}
