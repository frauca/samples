package frauca.combinations.sums;

import frauca.combinations.sums.operations.AddOperation;
import frauca.combinations.sums.operations.SingleValue;
import frauca.combinations.sums.operations.SubstractOperation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static frauca.combinations.sums.OperationsUtils.listOfList;
import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CombinationsMainServiceTest {

    @Mock
    IntegerDeserializer serializer;
    @Mock
    CombinationsService combinationsService;
    @Spy
    OperationsSerializer deserializer = new OperationsSerializer();

    @InjectMocks
    CombinationsMainService service;



    @Test
    void empty() {
        assertThat(service.resolve("")).isEqualTo("");
        assertThat(service.resolve(null)).isEqualTo("");
    }

    @Test
    void single() {
        List<Integer> inputOfCombinations = emptyList();
        when(serializer.deserialize(anyString())).thenReturn(inputOfCombinations);
        when(combinationsService.combinations(any()))
                .thenReturn(listOfList(new SingleValue(1)));

        assertThat(service.resolve("1")).isEqualTo("1,1=[1]");
    }

    @Test
    void three() {
        List<Integer> inputOfCombinations = emptyList();
        when(serializer.deserialize(anyString())).thenReturn(inputOfCombinations);
        when(combinationsService.combinations(any()))
                .thenReturn(List.of(
                        List.of(new SingleValue(1),new AddOperation(2)),
                        List.of(new SingleValue(1),new AddOperation(2),new SubstractOperation(3)),
                        List.of(new SingleValue(1),new SubstractOperation(1))
                ));

        String result = service.resolve("123");
        assertThat(result)
                .isEqualTo("0,2=[1 + 2 - 3]\n" +
                        "0,2=[1 - 1]\n" +
                        "3,1=[1 + 2]");
    }
}