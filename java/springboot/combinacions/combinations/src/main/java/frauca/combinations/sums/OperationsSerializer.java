package frauca.combinations.sums;

import frauca.combinations.sums.operations.Operation;

import java.util.List;
import java.util.stream.Collectors;

import static org.apache.logging.log4j.util.Strings.EMPTY;

public class OperationsSerializer {

    public String serialize(List<Operation> operations){
        if(operations==null || operations.isEmpty()){
            return EMPTY;
        }
        return operations.stream()
                .map(o->o.toString())
                .collect(Collectors.joining(" ","[","]"));
    }
}
