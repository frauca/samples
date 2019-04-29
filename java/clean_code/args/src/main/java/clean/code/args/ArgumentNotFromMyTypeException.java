package clean.code.args;

public class ArgumentNotFromMyTypeException extends clean.code.args.ArgumentException {

  public ArgumentNotFromMyTypeException(String message, String argumentName) {
    super(message, argumentName);
  }
}
