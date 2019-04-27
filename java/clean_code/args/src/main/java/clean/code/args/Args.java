package clean.code.args;

import java.util.List;

/**
 * This class let parse args and get its values
 */
public class Args {

  private List<Argument> arguments;

  public Args(String configuration,String...args){
    ArgumentsParser parser = new ArgumentsParser();
    this.arguments = parser.parse(configuration,args);
  }

  public boolean getBoolean(String name){
    return getValueWithFindAndCast(name,String.format("%s must be an boolean",name));
  }

  public int getInt(String name){
    return getValueWithFindAndCast(name,String.format("%s must be an integer",name));
  }

  public String getString(String name){
    return getValueWithFindAndCast(name,String.format("%s must be an string",name));
  }

  protected <T> T getValueWithFindAndCast(String name, String castMessage){
    try {
      Argument<T> argument = (Argument<T>) findArgument(name);
      return argument.getValue();
    }catch (ClassCastException e){
      throw new ArgumentException(castMessage,e);
    }
  }

  private Argument findArgument(String argumentName){
    for(Argument argument:arguments){
      if(argument.getName().equals(argumentName)){
        return argument;
      }
    }
    throw new ArgumentException(String.format("Argument %s not found",argumentName));
  }
}
