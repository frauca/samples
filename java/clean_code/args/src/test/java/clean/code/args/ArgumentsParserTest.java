package clean.code.args;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.util.Map;
import org.junit.Test;

public class ArgumentsParserTest {
  clean.code.args.ArgumentsParser parser = new clean.code.args.ArgumentsParser();

  @Test
  public void whenOnlyOneBoolean_thenOneBoooleanListArgument(){
    Map<String, clean.code.args.Argument> args = parser.parse("l","l=true");
    clean.code.args.Argument lArgument = args.get("l");
    assertThat(args.size(),is(1));
    assertNotNull(lArgument);
    assertThat(lArgument.getName(),is("l"));
  }

  @Test
  public void givenString_whenBoolean_returnBoolean(){
    Map<String, clean.code.args.Argument> args = parser.parse("l","l=true");
    clean.code.args.Argument lArgument = args.get("l");
    assertThat(lArgument.getValue(),is(true));
    assertThat(lArgument.getName(),is("l"));
  }

  @Test
  public void givenInteger_whenIsNotBollean_thenRaiseError(){
    try {
      Map<String, clean.code.args.Argument> args = parser.parse("l#","l=notAnInteger");
      fail();
    }catch (Exception e){
      assertThat(e,instanceOf(clean.code.args.ArgumentException.class));
    }
  }

}
