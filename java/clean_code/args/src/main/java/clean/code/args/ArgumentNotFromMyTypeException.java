package clean.code.args;

public class ArgumentNotFromMyTypeException extends ArgumentException {

  public ArgumentNotFromMyTypeException(String message, String argumentName) {
    super(message, argumentName);
  }
}
