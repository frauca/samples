package frauca.combinations.sums;

import frauca.combinations.sums.operations.AddOperation;
import frauca.combinations.sums.operations.Operation;
import frauca.combinations.sums.operations.SingleValue;
import frauca.combinations.sums.operations.SubstractOperation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CombinationsService {

    private final SimplificatorHelper simplifier = new SimplificatorHelper();

    public List<List<Operation>> combinations(List<Integer> values) {
        if (values == null || values.isEmpty()) {
            return Collections.emptyList();
        }
        return simplifier.simplify(
                internalCombinator(
                        values, Collections.emptyList()
                )
        );
    }

    private List<List<Operation>> internalCombinator(List<Integer> values, List<List<Operation>> rightOperations) {
        List<List<Operation>> result = new ArrayList<>();
        if (rightOperations.isEmpty()) {
            result.add(Collections.singletonList(SingleValue.from(values)));
        }
        if (!values.isEmpty()) {
            int last = values.size() - 1;
            result.addAll(
                    internalCombinator(
                            values.subList(0, last), addAndSubstract(values.get(last), rightOperations)
                    )
            );
        }else{
            result.addAll(rightOperations);
        }
        return result;
    }

    private List<List<Operation>> addAndSubstract(int value, List<List<Operation>> rightOperations) {
        if (rightOperations.isEmpty()) {
            rightOperations = Collections.singletonList(Collections.emptyList());
        }
        return rightOperations.stream()
                .flatMap(operations -> addAndSubstractOne(value, operations))
                .collect(Collectors.toList());
    }

    private Stream<List<Operation>> addAndSubstractOne(int value, List<Operation> operations) {
        AddOperation add = new AddOperation(value);
        SubstractOperation substract = new SubstractOperation(value);
        if (operations.isEmpty()) {
            return Stream.of(Collections.singletonList(add), Collections.singletonList(substract));
        }
        SingleValue single = new SingleValue(value);
        return combine(
                List.of(add, substract, single), operations
        );
    }

    private Stream<List<Operation>> combineList(List<Operation> left, List<List<Operation>> right) {
        return right.stream()
                .flatMap(rightList -> this.combine(left, rightList));
    }

    private Stream<List<Operation>> combine(List<Operation> left, List<Operation> right) {
        return left.stream()
                .map(operation -> operation.combine(right));
    }
}
