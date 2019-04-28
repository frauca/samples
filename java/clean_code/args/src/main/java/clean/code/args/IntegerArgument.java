package clean.code.args;

public class IntegerArgument extends ArgumentEndingTag<Integer> {
  private static String INTEGER_ENDING ="#";

  @Override
  public Argument<Integer> parseValue(String value) {
    try{
      int integerValue = Integer.valueOf(value);
      return new IntegerArgument(getName(),integerValue);
    }catch (NumberFormatException e){
      throw new ArgumentWithInvalidFormatException(String.format("%s is not an integer",value),getName(),e);
    }
  }

  @Override
  protected Argument<Integer> makeConfiguration(String argumentName) {
    return new IntegerArgument(argumentName);
  }

  public IntegerArgument(String name, Integer value) {
    super(INTEGER_ENDING,name, value);
  }

  public IntegerArgument(String name){super(INTEGER_ENDING,name);}
}
