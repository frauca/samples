package clean.code.args;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.util.Map;
import org.junit.Test;

public class ArgumentsParserTest {
  ArgumentsParser parser = new ArgumentsParser();

  @Test
  public void whenOnlyOneBoolean_thenOneBoooleanListArgument(){
    Map<String,Argument> args = parser.parse("l","l=true");
    Argument lArgument = args.get("l");
    assertThat(args.size(),is(1));
    assertNotNull(lArgument);
    assertThat(lArgument.getName(),is("l"));
  }

  @Test
  public void givenString_whenBoolean_returnBoolean(){
    Map<String,Argument> args = parser.parse("l","l=true");
    Argument lArgument = args.get("l");
    assertThat(lArgument.getValue(),is(true));
    assertThat(lArgument.getName(),is("l"));
  }

  @Test
  public void givenInteger_whenIsNotBollean_thenRaiseError(){
    try {
      Map<String,Argument> args = parser.parse("l#","l=notAnInteger");
      fail();
    }catch (Exception e){
      assertThat(e,instanceOf(ArgumentException.class));
    }
  }

}
