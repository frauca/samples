package clean.code.args;

import java.util.Map;

/**
 * This class let parse args and get its values
 */
public class Args {

  private Map<String,Argument> arguments;

  public Args(String configuration,String...args){
    ArgumentsParser parser = new ArgumentsParser();
    this.arguments = parser.parse(configuration,args);
  }

  public boolean getBoolean(String name){
    return getValueAndCast(name,String.format("%s must be an boolean",name));
  }

  public int getInt(String name){
    return getValueAndCast(name,String.format("%s must be an integer",name));
  }

  public String getString(String name){
    return getValueAndCast(name,String.format("%s must be an string",name));
  }

  private<T> T getValueAndCast(String name, String castMessage){
    try {
      Argument<T> argument = getArgument(name);
      return argument.getValue();
    }catch (ClassCastException e){
      throw new ArgumentException(castMessage,e);
    }
  }

  private <T> Argument<T> getArgument(String name){
    Argument<T> argument = arguments.get("name");
    if(argument==null){
      throw new ArgumentException(String.format("Argument %s not found",name));
    }
    return argument;
  }

}
