package clean.code.args.helper;

import static org.junit.Assert.fail;

import clean.code.args.Argument;
import clean.code.args.BooleanArgument;
import clean.code.args.IntegerArgument;
import java.util.List;
import org.junit.Test;

public class AllArgumentTypeTest {
  AllArgumentType allArgument=new AllArgumentType();

  @Test
  public void whenAllArgumentType_thenBooleanIsPresent(){
    List<Argument> allArguments = allArgument.getAllArgumentTypes();
  }

  @Test
  public void givenAllTypes_thenArgumentTypedGoBefforedNonTagged(){
    List<Argument> allArguments = allArgument.getAllArgumentTypes();
    for(Argument argument:allArguments){
      if(argument instanceof IntegerArgument){
        break;
      }else if(argument instanceof BooleanArgument){
        fail();
      }
    }
  }

}
