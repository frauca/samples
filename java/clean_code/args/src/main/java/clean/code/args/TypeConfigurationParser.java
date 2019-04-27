package clean.code.args;

public abstract class TypeConfigurationParser<T> {

  public abstract Configuration<T> parse(String argumentName) throws ArgumentException;

}
