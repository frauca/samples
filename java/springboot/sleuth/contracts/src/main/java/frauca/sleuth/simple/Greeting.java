package frauca.sleuth.simple;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value
@Builder
@Jacksonized
public class Greeting {
    String message;

    public static Greeting say(String message) {
        return Greeting.builder()
                .message(message)
                .build();
    }
}
