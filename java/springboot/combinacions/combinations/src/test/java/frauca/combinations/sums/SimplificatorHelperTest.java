package frauca.combinations.sums;

import frauca.combinations.sums.operations.AddOperation;
import frauca.combinations.sums.operations.Operation;
import frauca.combinations.sums.operations.SingleValue;
import frauca.combinations.sums.operations.SubstractOperation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class SimplificatorHelperTest extends OperationsUtils {

    @InjectMocks
    SimplificatorHelper simplificatorHelper;

    @Test
    void emptyOrNull() {
        assertThat(simplificatorHelper.simplify(null)).isEmpty();
        assertThat(simplificatorHelper.simplify(emptyList())).isEmpty();
    }

    @Test
    void oneElement_thenSingleValue() {
        assertThat(simplificatorHelper.simplify(listOfList(
                new SingleValue(1)
        ))).containsExactly(singletonList(new SingleValue(1)));
        assertThat(simplificatorHelper.simplify(listOfList(
                new AddOperation(1)
        ))).containsExactly(singletonList(new SingleValue(1)));
        assertThat(simplificatorHelper.simplify(listOfList(
                new SubstractOperation(1)
        ))).containsExactly(singletonList(new SingleValue(1)));
    }

    @Test
    void twoElementsSimplification() {
        List<List<Operation>> operations = List.of(
                List.of(new AddOperation(1), new AddOperation(2)),
                List.of(new SubstractOperation(1), new AddOperation(2))

        );
        List<List<Operation>> result = simplificatorHelper.simplify(operations);
        assertThat(result).hasSize(1);
        assertThat(result)
                .containsExactly(
                        List.of(new SingleValue(1),new AddOperation(2)));
    }
}