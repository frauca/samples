package frauca.combinations.sums.operations;

import lombok.AllArgsConstructor;
import lombok.Value;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Value
@AllArgsConstructor
public class SingleValue implements Operation {
    private final Integer value;

    public static SingleValue from(List<Integer> integers) {
        int value = 0;
        if (integers != null) {
            int exponent = 1;
            for (int i = integers.size() - 1; i >= 0; i--) {
                value = integers.get(i) * exponent + value;
                exponent *= exponentOf(integers.get(i)) * 10;
            }
        }
        return new SingleValue(value);
    }

    @Override
    public Integer apply(Integer value) {
        return this.value;
    }

    @Override
    public List<Operation> combine(List<Operation> operations) {
        if (operations == null || operations.isEmpty()) {
            return Collections.singletonList(this);
        } else {
            List<Operation> result = new ArrayList<>();
            result.add(combine(operations.get(0)));
            if (operations.size() > 1) {
                result.addAll(operations.subList(1, operations.size()));
            }
            return result;
        }
    }

    @Override
    public Operation setValue(int value) {
        return new SingleValue(value);
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }


    private Operation combine(Operation operation) {
        int combineValue =
                SingleValue.from(
                        List.of(this.getValue(), operation.getValue())
                ).getValue();
        return operation.setValue(combineValue);
    }

    private static Integer exponentOf(Integer number) {
        double rawexp = Math.floor(Math.log10(number));
        return (int) Math.pow(10, rawexp);
    }
}
