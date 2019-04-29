package clean.code.args;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import org.junit.Test;


public class ArgsTest {

  @Test
  public void useCase_3arguments_IntegerBoolean(){
    clean.code.args.Args arg = new clean.code.args.Args("l,p#,d*", "l=true","p=8080","d=mypath");
    assertThat(arg.getBoolean("l"),is(true));
    assertThat(arg.getInt("p"),is(8080));
    assertThat(arg.getString("d"),is("mypath"));
  }

  @Test
  public void givenArgs_whenNoMarkAndValidValue_thenVolean(){
    clean.code.args.Args args = new clean.code.args.Args("l","l=true");
    assertThat(args.getBoolean("l"), is(true));
  }

  @Test
  public void givenConfig_whenAskForInexistentValue_thenException(){
    clean.code.args.Args args = new clean.code.args.Args("l","l=true");
    try{
      args.getBoolean("p");
      fail();
    }catch (Exception e){
      assertThat(e,instanceOf(clean.code.args.ArgumentException.class));
    }
  }
  @Test
  public void givenConfig_whenArgumentNotDefined_thenException(){

    try{
      clean.code.args.Args args = new clean.code.args.Args("l","p=true");
      fail();
    }catch (Exception e){
      assertThat(e,instanceOf(clean.code.args.ArgumentException.class));
    }
  }

  @Test
  public void givenConfig_whenInteger_thenIntegerCouldBeRetrivied(){
    clean.code.args.Args args = new clean.code.args.Args("l#","l=1");
    assertThat(args.getInt("l"), is(1));
  }

  @Test
  public void givenConfig_whenString_thenStringCouldBeRetrived(){
    clean.code.args.Args args = new clean.code.args.Args("l*","l=hello");
    assertThat(args.getString("l"), is("hello"));
  }
}
