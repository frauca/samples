package clean.code.args;

public class BooleanArgument extends clean.code.args.Argument<Boolean> {

  @Override
  public clean.code.args.Argument<Boolean> parseValue(String value) {
    Boolean argumentValue = Boolean.valueOf(value);
    return new BooleanArgument(getName(),argumentValue);
  }

  @Override
  public clean.code.args.Argument<Boolean> parseConfiguration(String argumentName)
      throws clean.code.args.ArgumentNotFromMyTypeException {
    return new BooleanArgument(argumentName);
  }

  public BooleanArgument(String name, Boolean value) {
    super(name, value);
  }
  public BooleanArgument(String name){super(name);}
}
