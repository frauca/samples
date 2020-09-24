package frauca.combinations.sums;

import frauca.combinations.sums.operations.Operation;
import frauca.combinations.sums.operations.SingleValue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class SimplificatorHelper {

    public List<List<Operation>> simplify(List<List<Operation>> operations){
        if(operations==null){
            return Collections.emptyList();
        }
        return _simplify(operations);
    }

    private List<List<Operation>> _simplify(List<List<Operation>> operations){
        return operations.stream()
                .map(this::firstBySingle)
                .distinct()
                .collect(Collectors.toList());
    }

    private List<Operation> firstBySingle(List<Operation> operations){
        List<Operation> result = new ArrayList<>();
        result.add(new SingleValue(operations.get(0).getValue()));
        if(operations.size()>0){
            result.addAll(operations.subList(1,operations.size()));
        }
        return result;
    }
}
