package clean.code.args;

public abstract class Configuration<T> implements Comparable<Configuration>  {

  private final String argumentName;

  public abstract Argument<T> parse(String value);

  public Configuration(String argumentName) {
    this.argumentName = argumentName;
  }

  public String getArgumentName(){
    return argumentName;
  }

  @Override
  public int compareTo(Configuration o) {
    return getArgumentName().compareTo(o.getArgumentName());
  }
}
