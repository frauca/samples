package frauca.oauth.http;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class PagesController {

    @GetMapping("/app")
    public String app(){
        log.info("Call app page");
        return "This is the app you need.";
    }

    @GetMapping("/user1")
    public String user1(){
        log.info("User 1 page");
        return "This is the only visible by user1.";
    }

    @GetMapping("/user2")
    public String user2(){
        log.info("User 2 page");
        return "This is the only visible by user2.";
    }
}
