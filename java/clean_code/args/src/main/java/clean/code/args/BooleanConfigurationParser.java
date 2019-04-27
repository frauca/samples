package clean.code.args;

public class BooleanConfigurationParser extends TypeConfigurationParser<Boolean> {

  @Override
  public Configuration<Boolean> parse(String argumentName) {
    return new BooleanConfiguration(argumentName);
  }
}
