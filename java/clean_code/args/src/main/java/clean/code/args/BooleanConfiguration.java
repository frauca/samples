package clean.code.args;

public class BooleanConfiguration extends Configuration<Boolean> {

  @Override
  public Argument<Boolean> parse(String value) {
    Boolean argumentValue = Boolean.valueOf(value);
    return new BooleanArgument(getArgumentName(),argumentValue);
  }

  public BooleanConfiguration(String argumentName) {
    super(argumentName);
  }
}
