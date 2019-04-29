package clean.code.args;

public abstract  class ArgumentEndingTag<T> extends clean.code.args.Argument<T> {

  private final String endingTag;

  protected abstract clean.code.args.Argument<T> makeConfiguration(String name);

  @Override
  public clean.code.args.Argument<T> parseConfiguration(String argumentName) throws clean.code.args.ArgumentException {
    if(isValid(argumentName)){
      return trimNameAndMakeConfig(argumentName);
    }
    throw new clean.code.args.ArgumentNotFromMyTypeException(String.format("%s argument is not of this type",argumentName),argumentName);
  }

  protected boolean isValid(String argumentName){
    return argumentName.endsWith(endingTag);
  }

  protected clean.code.args.Argument<T> trimNameAndMakeConfig(String argumentName){
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
