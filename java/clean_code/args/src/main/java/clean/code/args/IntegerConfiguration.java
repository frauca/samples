package clean.code.args;


public class IntegerConfiguration extends Configuration<Integer> {


  public IntegerConfiguration(String argumentName) {
    super(argumentName);
  }

  @Override
  public Argument<Integer> parse(String value) {
    try{
      int integerValue = Integer.valueOf(value);
      return new IntegerArgument(getArgumentName(),integerValue);
    }catch (NumberFormatException e){
      throw new ArgumentException(String.format("%s is not an integer",value),e);
    }

  }
}
