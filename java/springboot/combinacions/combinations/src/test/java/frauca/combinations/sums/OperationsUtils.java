package frauca.combinations.sums;

import frauca.combinations.sums.operations.Operation;

import java.util.List;

import static java.util.Collections.singletonList;

public class OperationsUtils {

    public static List<List<Operation>> listOfList(Operation operation) {
        return singletonList(
                singletonList(
                        operation
                )
        );
    }
}
