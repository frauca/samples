package clean.code.args;

public class StringConfigurationParser extends TypeConfigurationEndingTagParser<String> {

  protected StringConfigurationParser() {
    super("*");
  }

  @Override
  protected Configuration<String> makeConfiguration(String name) {
    return new StringConfiguration(name);
  }

}
