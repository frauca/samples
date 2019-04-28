package clean.code.args;

public class BooleanArgument extends Argument<Boolean> {

  @Override
  public Argument<Boolean> parseValue(String value) {
    Boolean argumentValue = Boolean.valueOf(value);
    return new BooleanArgument(getName(),argumentValue);
  }

  @Override
  public Argument<Boolean> parseConfiguration(String argumentName)
      throws ArgumentNotFromMyTypeException {
    return new BooleanArgument(argumentName);
  }

  public BooleanArgument(String name, Boolean value) {
    super(name, value);
  }
  public BooleanArgument(String name){super(name);}
}
