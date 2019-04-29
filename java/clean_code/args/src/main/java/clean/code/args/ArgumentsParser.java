package clean.code.args;

import clean.code.args.helper.AllArgumentType;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * To add one Type implement the argument type and and a dummy reference to the aviableTypes
 */
public class ArgumentsParser {

  public static String SINGLE_ARGUMENT_SEPARATOR="=";
  public static String ARGUMENTS_CONFIGURATION_SEPARATOR=",";
  private final List<Argument> aviableTypes;

  public Map<String, clean.code.args.Argument> parse(String configuration,String...args){
    Map<String, clean.code.args.Argument> configurations = parseConfigurations(configuration);
    Map<String, clean.code.args.Argument> arguments = configurations;
    for(String argument:args)
      arguments = setArgumentValueOnConfigurations(argument,configurations);
    return arguments;
  }

  public Map<String, clean.code.args.Argument> parseConfigurations(String configuration){
    Map<String, clean.code.args.Argument> configurations = new HashMap();
    for(String argumentConfiguration:configuration.split(ARGUMENTS_CONFIGURATION_SEPARATOR)) {
      clean.code.args.Argument configurationArgument = parseOneConfiguration(argumentConfiguration);
      configurations.put(configurationArgument.getName(), configurationArgument);
    }
    return configurations;
  }

  protected Map<String, clean.code.args.Argument> setArgumentValueOnConfigurations(String argument, Map<String, clean.code.args.Argument> configurations){
    String[] tuple = getTupleFromArgument(argument);
    return setArgumentValue(tuple,configurations);
  }

  private String[] getTupleFromArgument(String fullArgument){
    String[] tuple = fullArgument.split(SINGLE_ARGUMENT_SEPARATOR,2);
    if(tuple.length<=1){
      throw new clean.code.args.ArgumentException(String.format("Invalide %s argument. It must be <varname>=<value>",fullArgument));
    }
    return tuple;
  }


  private clean.code.args.Argument parseOneConfiguration(String argumentConfiguration){
    for(clean.code.args.Argument argumentType:aviableTypes){
      try {
        return argumentType.parseConfiguration(argumentConfiguration);
      }catch (clean.code.args.ArgumentNotFromMyTypeException e){

      }
    }
    throw new clean.code.args.ArgumentException(String.format("The argument %s is not suported"));
  }



  private Map<String, clean.code.args.Argument> setArgumentValue(String[] tuple,Map<String, clean.code.args.Argument> configurations){
    String argumentName=tuple[0];
    String argumentValue=tuple[1];
    clean.code.args.Argument argument = configurations.get(argumentName);
    if(argument==null)
      throw new clean.code.args.ArgumentNotFromMyTypeException(String.format("Argument %s not expected.",argumentName),argumentName);
    clean.code.args.Argument argumentWithValue = argument.parseValue(argumentValue);
    configurations.put(argumentName,argumentWithValue);
    return configurations;
  }

  public ArgumentsParser(){
    AllArgumentType alltypesFinder = new AllArgumentType();
    this.aviableTypes = alltypesFinder.getAllArgumentTypes();
  }
}
