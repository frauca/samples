package clean.code.args;

import static junit.framework.TestCase.assertTrue;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.junit.Test;

public class ArgumentsParserTest {
  ArgumentsParser parser = new ArgumentsParser();
  ConfigurationParser configurationParser = new ConfigurationParser();

  @Test
  public void whenOnlyOneBoolean_thenOneBoooleanListArgument(){
    List<Argument> args = parser.parse("l","l=true");
    Optional<Argument> firstArgument = args.stream().findFirst();
    assertThat(args.size(),is(1));
    assertTrue(firstArgument.isPresent());
    assertThat(firstArgument.get().getName(),is("l"));
  }

  @Test
  public void givenString_whenBoolean_returnBoolean(){
    Set<Configuration> configurations = configurationParser.parse("l");
    Argument argument = parser.parseOneOverConfigurations("l=true",configurations);
    assertThat(argument.getValue(),is(true));
    assertThat(argument.getName(),is("l"));
  }

  @Test
  public void givenInteger_whenIsNotBollean_thenRaiseError(){
    Set<Configuration> configurations = configurationParser.parse("l#");
    try {
      Argument argument = parser.parseOneOverConfigurations("l=notAnInteger", configurations);
      fail();
    }catch (Exception e){
      assertThat(e,instanceOf(ArgumentException.class));
    }
  }
}
