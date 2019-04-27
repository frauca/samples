package clean.code.args;

public class StringConfiguration extends Configuration<String> {

  public StringConfiguration(String argumentName) {
    super(argumentName);
  }

  @Override
  public Argument<String> parse(String value) {
    return new StringArgument(getArgumentName(),value);
  }
}
