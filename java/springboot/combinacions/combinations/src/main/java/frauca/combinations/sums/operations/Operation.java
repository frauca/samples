package frauca.combinations.sums.operations;

import java.util.List;

public interface Operation {
    Integer getValue();

    Integer apply(Integer value);

    List<Operation> combine(List<Operation> operations);

    Operation setValue(int value);
}
