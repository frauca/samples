package frauca.combinations.sums;

import frauca.combinations.sums.operations.AddOperation;
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
class OperationsSerializerTest {

    @InjectMocks
    OperationsSerializer deserializer;

    @Test
    void empty() {
        assertThat(deserializer.serialize(null)).isEqualTo("");
        assertThat(deserializer.serialize(emptyList())).isEqualTo("");
    }

    @Test
    void oneValue() {
        assertThat(deserializer.serialize(singletonList(new SingleValue(1)))).isEqualTo("[1]");
    }

    @Test
    void twoalues() {
        assertThat(deserializer.serialize(
                List.of(new SingleValue(1),new AddOperation(2)))).isEqualTo("[1 + 2]");
        assertThat(deserializer.serialize(
                List.of(new SingleValue(1),new SubstractOperation(2)))).isEqualTo("[1 - 2]");
    }
}