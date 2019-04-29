package clean.code.args;

public class StringArgument extends clean.code.args.ArgumentEndingTag<String> {
  private static String STRING_ENDING = "*";

  @Override
  public clean.code.args.Argument<String> parseValue(String value) {
    return new StringArgument(getName(),value);
  }

  @Override
  protected clean.code.args.Argument<String> makeConfiguration(String name) {
    return new StringArgument(name);
  }


  public StringArgument(String name, String value) {
    super(STRING_ENDING,name, value);
  }
  public StringArgument(String name){
    super(STRING_ENDING,name);
  }
}
