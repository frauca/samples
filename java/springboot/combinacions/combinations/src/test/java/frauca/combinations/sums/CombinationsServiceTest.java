package frauca.combinations.sums;

import frauca.combinations.sums.operations.AddOperation;
import frauca.combinations.sums.operations.Operation;
import frauca.combinations.sums.operations.SingleValue;
import frauca.combinations.sums.operations.SubstractOperation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class CombinationsServiceTest {
    @InjectMocks
    CombinationsService service;

    @Test
    void empty(){
        assertThat(service.combinations(null)).isEqualTo(Collections.emptyList());
        assertThat(service.combinations(Collections.emptyList())).isEqualTo(Collections.emptyList());
    }

    @Test
    void single(){
        List<List<Operation>> result = service.combinations(Collections.singletonList(1));
        assertThat(result).hasSize(1);
        assertThat(result.get(0)).containsExactly(new SingleValue(1));
    }

    @Test
    void simple(){
        List<List<Operation>> result = service.combinations(List.of(1,2));
        assertThat(result).hasSize(3);
        assertThat(result.get(0)).containsExactly(new SingleValue(12));
        assertThat(result.get(1)).containsExactly(new SingleValue(1),new AddOperation(2));
        assertThat(result.get(2)).containsExactly(new SingleValue(1),new SubstractOperation(2));
    }
    @Test
    void threeElements(){
        List<List<Operation>> result = service.combinations(List.of(1,2,3));
        assertThat(result).hasSize(9);
        assertThat(result.get(0)).containsExactly(new SingleValue(123));
        assertThat(result.get(1)).containsExactly(new SingleValue(1),new AddOperation(2),new AddOperation(3));
        assertThat(result.get(2)).containsExactly(new SingleValue(12),new AddOperation(3));
        assertThat(result.get(3)).containsExactly(new SingleValue(1),new SubstractOperation(2),new AddOperation(3));
        assertThat(result.get(4)).containsExactly(new SingleValue(1),new AddOperation(23));
        assertThat(result.get(5)).containsExactly(new SingleValue(1),new AddOperation(2),new SubstractOperation(3));
        assertThat(result.get(6)).containsExactly(new SingleValue(12),new SubstractOperation(3));
        assertThat(result.get(7)).containsExactly(new SingleValue(1),new SubstractOperation(2),new SubstractOperation(3));
        assertThat(result.get(8)).containsExactly(new SingleValue(1),new SubstractOperation(23));
    }
}