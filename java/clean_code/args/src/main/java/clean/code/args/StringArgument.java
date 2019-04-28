package clean.code.args;

public class StringArgument extends ArgumentEndingTag<String> {
  private static String STRING_ENDING = "*";

  @Override
  public Argument<String> parseValue(String value) {
    return new StringArgument(getName(),value);
  }

  @Override
  protected Argument<String> makeConfiguration(String name) {
    return new StringArgument(name);
  }

  @Override
  public Argument<String> parseConfiguration(String argumentName)
      throws ArgumentNotFromMyTypeException {
    return new StringArgument(argumentName);
  }

  public StringArgument(String name, String value) {
    super(STRING_ENDING,name, value);
  }
  public StringArgument(String name){
    super(STRING_ENDING,name);
  }
}
