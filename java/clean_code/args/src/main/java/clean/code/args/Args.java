package clean.code.args;

import java.util.Map;

/**
 * This class let parse args and get its values
 */
public class Args {

  private Map<String, clean.code.args.Argument> arguments;

  public Args(String configuration,String...args){
    clean.code.args.ArgumentsParser parser = new clean.code.args.ArgumentsParser();
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
      clean.code.args.Argument<T> argument = getArgument(name);
      return argument.getValue();
    }catch (ClassCastException e){
      throw new clean.code.args.ArgumentException(castMessage,e);
    }
  }

  private <T> clean.code.args.Argument<T> getArgument(String name){
    clean.code.args.Argument<T> argument = arguments.get(name);
    if(argument==null){
      throw new clean.code.args.ArgumentException(String.format("Argument %s not found",name));
    }
    return argument;
  }

}
