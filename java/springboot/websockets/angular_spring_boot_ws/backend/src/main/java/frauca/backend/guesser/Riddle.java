package frauca.backend.guesser;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value
@Builder(toBuilder = true)
@Jacksonized
public class Riddle {
    String name;
    int max;
    int min;
    AnswerEvaluation result;
}
