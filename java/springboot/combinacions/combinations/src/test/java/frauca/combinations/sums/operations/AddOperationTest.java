package frauca.combinations.sums.operations;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class AddOperationTest {

    @Test
    void operations(){
        assertThat(new AddOperation(1).apply(1)).isEqualTo(2);
    }

    @Test
    void combine(){
        List<Operation> operations = List.of(
                new SingleValue(1),
                new AddOperation(2),
                new SubstractOperation(3));

        List<Operation> result = new AddOperation(4).combine(operations);

        assertThat(result).hasSize(4);
        assertThat(result).containsExactly(
                new AddOperation(4),
                new SingleValue(1),
                new AddOperation(2),
                new SubstractOperation(3)
        );
    }
}