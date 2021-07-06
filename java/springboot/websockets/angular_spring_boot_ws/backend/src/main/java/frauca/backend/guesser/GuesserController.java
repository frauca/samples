package frauca.backend.guesser;

import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class GuesserController {

    private final GuesserService guesser;

    public GuesserController(GuesserService guesser) {
        this.guesser = guesser;
    }

    @PostMapping("/api/guesser")
    @CrossOrigin
    @MessageMapping("/guesser")
    @SendTo("/topic/guesses")
    public Answer guess(@RequestBody @Payload Riddle riddle) {
        return guesser.guest(riddle);
    }
}
