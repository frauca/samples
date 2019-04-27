package clean.code.args;

public abstract  class TypeConfigurationEndingTagParser<T> extends TypeConfigurationParser<T> {

  private final String endingTag;

  protected abstract Configuration<T> makeConfiguration(String name);

  @Override
  public  Configuration<T> parse(String argumentName) throws ArgumentException{
    if(isValid(argumentName)){
      return trimNameAndMakeConfig(argumentName);
    }
    throw new ArgumentException(String.format("%s argument is not of this type",argumentName));
  }

  protected boolean isValid(String argumentName){
    return argumentName.endsWith(endingTag);
  }

  protected Configuration<T> trimNameAndMakeConfig(String argumentName){
    String newArgumentName=argumentName.substring(0,argumentName.length()-1);
    return makeConfiguration(newArgumentName);
  }

  protected TypeConfigurationEndingTagParser(String endingTag) {
    this.endingTag = endingTag;
  }

}
