package frauca.combinations.sums.operations;

import org.junit.jupiter.api.Test;

import java.util.List;

import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;

class SubstractOperationTest {

    @Test
    void operations() {
        assertThat(new SubstractOperation(1).apply(1)).isEqualTo(0);
    }

    @Test
    void combine() {
        List<Operation> operations;
        operations = singletonList(new SingleValue(1));

        List<Operation> result = new SubstractOperation(4).combine(operations);

        assertThat(result).hasSize(2);
        assertThat(result).containsExactly(
                new SubstractOperation(4),
                new SingleValue(1)
        );
    }
}