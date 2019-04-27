package clean.code.args;

public class IntegerConfigurationParser extends TypeConfigurationEndingTagParser<Integer> {

  protected IntegerConfigurationParser() {
    super("#");
  }

  @Override
  protected Configuration<Integer> makeConfiguration(String name) {
    return new IntegerConfiguration(name);
  }
}
