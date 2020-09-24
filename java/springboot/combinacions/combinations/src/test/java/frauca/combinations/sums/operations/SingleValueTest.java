package frauca.combinations.sums.operations;

import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;

class SingleValueTest {

    @Test
    void operations() {
        assertThat(new SingleValue(1).apply(1)).isEqualTo(1);
    }

    @Test
    void singleValueFromIntegers() {
        assertThat(SingleValue.from(null)).isEqualTo(new SingleValue(0));
        assertThat(SingleValue.from(Collections.emptyList())).isEqualTo(new SingleValue(0));
        assertThat(SingleValue.from(singletonList(1))).isEqualTo(new SingleValue(1));
        assertThat(SingleValue.from(List.of(1, 2))).isEqualTo(new SingleValue(12));
        assertThat(SingleValue.from(List.of(1, 2, 3))).isEqualTo(new SingleValue(123));

    }

    @Test
    void combine() {
        List<Operation> operations = List.of(
                new AddOperation(2),
                new SubstractOperation(3));

        List<Operation> result = new SingleValue(1).combine(operations);

        assertThat(result).hasSize(2);
        assertThat(result).containsExactly(
                new AddOperation(12),
                new SubstractOperation(3)
        );
    }

    @Test
    void combineBigger() {
        List<Operation> operations = singletonList(new AddOperation(234));

        List<Operation> result = new SingleValue(1).combine(operations);

        assertThat(result).hasSize(1);
        assertThat(result).containsExactly(
                new AddOperation(1234)
        );
    }
}