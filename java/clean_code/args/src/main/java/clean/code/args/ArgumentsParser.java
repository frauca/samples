package clean.code.args;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ArgumentsParser {

  public static String SEPARATOR="=";
  ConfigurationParser configurationParser  = new ConfigurationParser();

  public List<Argument> parse(String configuration,String...args){
    Set<Configuration> configurations = configurationParser.parse(configuration);
    List<Argument> arguments = new ArrayList<>();
    for(String argument:args)
      arguments.add(parseOneOverConfigurations(argument,configurations));
    return arguments;
  }

  protected Argument parseOneOverConfigurations(String argument,Set<Configuration> configurations){
    String[] tuple = argument.split(SEPARATOR,2);
    return findTupleInConfigurations(tuple,configurations);
  }

  private Argument findTupleInConfigurations(String[] tuple,Set<Configuration> configurations){
    String argumentName=tuple[0];
    String argumentValue=tuple[1];
    for(Configuration configuration:configurations){
      if(configuration.getArgumentName().equals(argumentName)){
        return configuration.parse(argumentValue);
      }
    }
    throw new ArgumentException(String.format("No argument %s has been configured",argumentName));
  }

}
