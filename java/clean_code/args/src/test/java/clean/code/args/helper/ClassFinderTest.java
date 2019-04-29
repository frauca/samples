package clean.code.args.helper;

import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

import clean.code.args.Args;
import clean.code.args.Argument;
import clean.code.args.BooleanArgument;
import java.util.List;
import org.junit.Test;

public class ClassFinderTest {

  ClassFinder classFinder = new ClassFinder();

  @Test
  public void givenThisProject_whenAskForClasses_thenArgsClassIsPressent(){
    List<Class> allClasses = classFinder.getAllClasses("clean.code.args");
    assertThat(allClasses,hasItems(Args.class));
  }

  @Test
  public void givenClassOfProjct_whenAskForArgument_thenArgumentClassesIsReturned(){
    List<Class> allArguments = classFinder.getAllClasses("clean.code.args", Argument.class);
    assertThat(allArguments,hasItems(BooleanArgument.class));
    assertThat(allArguments,not(hasItems(Args.class)));
  }
}
