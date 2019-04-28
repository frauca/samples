package clean.code.args;

public abstract  class ArgumentEndingTag<T> extends Argument<T> {

  private final String endingTag;

  protected abstract Argument<T> makeConfiguration(String name);

  @Override
  public  Argument<T> parseConfiguration(String argumentName) throws ArgumentException{
    if(isValid(argumentName)){
      return trimNameAndMakeConfig(argumentName);
    }
    throw new ArgumentException(String.format("%s argument is not of this type",argumentName));
  }

  protected boolean isValid(String argumentName){
    return argumentName.endsWith(endingTag);
  }

  protected Argument<T> trimNameAndMakeConfig(String argumentName){
    String newArgumentName=argumentName.substring(0,argumentName.length()-1);
    return makeConfiguration(newArgumentName);
  }

  protected ArgumentEndingTag(String endingTag,String argumentName,T value){
    super(argumentName,value);
    this.endingTag=endingTag;
  }

  protected ArgumentEndingTag(String endingTag,String argumentName) {
    super(argumentName);
    this.endingTag = endingTag;
  }

}
