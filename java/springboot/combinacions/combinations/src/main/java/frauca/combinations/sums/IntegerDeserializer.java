package frauca.combinations.sums;

import java.io.InvalidObjectException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class IntegerDeserializer {

    public List<Integer> deserialize(String input){
        if(input==null){
            return  Collections.emptyList();
        }
        return input.chars()
                .mapToObj(c-> {
                    if (Character.isDigit(c)) {
                        return Character.getNumericValue(c);
                    }
                    throw new RuntimeException("Only integers are valid");
                }
            ).collect(Collectors.toList());
    }
}
