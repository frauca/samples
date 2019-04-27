package clean.code.args;

import java.util.Set;
import java.util.TreeSet;

public class ConfigurationParser {

  public static String SEPARATOR=",";
  protected TypeConfigurationParser[]aviableTypes ={
      new IntegerConfigurationParser(),
      new StringConfigurationParser(),
      new BooleanConfigurationParser()
  };


  public Set<Configuration> parse(String configuration){
    Set<Configuration> configurations = new TreeSet<>();
    for(String argumentConfiguration:configuration.split(SEPARATOR))
      configurations.add(parseOne(argumentConfiguration));
    return configurations;
  }

  private Configuration parseOne(String argumentConfiguration){
    for(TypeConfigurationParser typeConfiguration:aviableTypes){
      try {
        return typeConfiguration.parse(argumentConfiguration);
      }catch (ArgumentException e){

      }
    }
    throw new ArgumentException(String.format("The argument %s is invalid as it is not suported"));
  }
}
