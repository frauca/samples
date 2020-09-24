package frauca.combinations.sums.operations;

import lombok.AllArgsConstructor;
import lombok.Value;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@Value
@AllArgsConstructor
public class SubstractOperation implements Operation {
    private final Integer value;

    @Override
    public Integer apply(Integer value) {
        return this.value - value;
    }

    @Override
    public List<Operation> combine(List<Operation> operations) {
        List<Operation> result = new ArrayList<>(operations);
        result.add(0,this);
        return result;
    }

    @Override
    public Operation setValue(int value) {
        return new SubstractOperation(value);
    }

    @Override
    public String toString() {
        return "- "+value;
    }
}
