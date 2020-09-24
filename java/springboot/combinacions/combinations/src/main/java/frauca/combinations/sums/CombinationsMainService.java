package frauca.combinations.sums;

import frauca.combinations.sums.operations.Operation;
import lombok.AllArgsConstructor;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;
import static org.apache.logging.log4j.util.Strings.EMPTY;

@AllArgsConstructor
public class CombinationsMainService {

    private final IntegerDeserializer deserializer;
    private final CombinationsService combinationsService;
    private final OperationsSerializer serializer;

    public String resolve(String input) {
        if (StringUtils.isEmpty(input)) {
            return EMPTY;
        }
        return resolve(deserializer.deserialize(input));
    }

    private String resolve(List<Integer> values) {
        return groupByResult(
                combinationsService.combinations(values)
        ).entrySet()
                .stream()
                .map(this::serialize)
                .collect(Collectors.joining("\n"));
    }

    private String serialize(Map.Entry<Integer, List<List<Operation>>> oneResultOptions) {
        int result = oneResultOptions.getKey();
        int numOfSolutions = oneResultOptions.getValue().size();
        return oneResultOptions.getValue().stream()
                .map(serializer::serialize)
                .map(array -> String.format("%s,%s=%s", result, numOfSolutions, array))
                .collect(Collectors.joining("\n"));
    }

    private Map<Integer, List<List<Operation>>> groupByResult(List<List<Operation>> operations) {
        return operations.stream()
                .collect(groupingBy(this::solve));
    }

    private Integer solve(List<Operation> operations) {
        int total = 0;
        for (Operation operation : operations) {
            total = operation.apply(total);
        }
        return total;
    }
}
