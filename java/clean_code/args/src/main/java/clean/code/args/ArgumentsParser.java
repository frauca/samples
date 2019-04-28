package clean.code.args;

import java.util.HashMap;
import java.util.Map;

/**
 * To add one Type implement the argument type and and a dummy reference to the aviableTypes
 */
public class ArgumentsParser {

  public static String SEPARATOR="=";
  private final Argument[] aviableTypes = {
      new IntegerArgument("integerType"),
      new StringArgument("stringType"),
      new BooleanArgument("booleanType")
  };

  public Map<String,Argument> parse(String configuration,String...args){
    Map<String,Argument> configurations = parseConfigurations(configuration);
    Map<String,Argument> arguments = configurations;
    for(String argument:args)
      arguments = setArgumentValueOnConfigurations(argument,configurations);
    return arguments;
  }

  public Map<String, Argument> parseConfigurations(String configuration){
    Map<String,Argument> configurations = new HashMap();
    for(String argumentConfiguration:configuration.split(SEPARATOR)) {
      Argument configurationArgument = parseOneConfiguration(argumentConfiguration);
      configurations.put(configurationArgument.getName(), configurationArgument);
    }
    return configurations;
  }

  protected Map<String,Argument> setArgumentValueOnConfigurations(String argument, Map<String,Argument> configurations){
    String[] tuple = argument.split(SEPARATOR,2);
    return setArgumentValue(tuple,configurations);
  }


  private Argument parseOneConfiguration(String argumentConfiguration){
    for(Argument argumentType:aviableTypes){
      try {
        return argumentType.parseConfiguration(argumentConfiguration);
      }catch (ArgumentNotFromMyTypeException e){

      }
    }
    throw new ArgumentException(String.format("The argument %s is not suported"));
  }



  private Map<String,Argument> setArgumentValue(String[] tuple,Map<String, Argument> configurations){
    String argumentName=tuple[0];
    String argumentValue=tuple[1];
    Argument argument = configurations.get(argumentName);
    if(argument==null)
      throw new ArgumentNotFromMyTypeException(String.format("Argument %s not expected.",argumentName),argumentName);
    Argument argumentWithValue = argument.parseValue(argumentValue);
    configurations.put(argumentName,argumentWithValue);
    return configurations;
  }

}
