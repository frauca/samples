package clean.code.args;

public class ArgumentWithInvalidFormatException extends ArgumentException {

  public ArgumentWithInvalidFormatException(String message, String argumentName) {
    super(message, argumentName);
  }
  public ArgumentWithInvalidFormatException(String message, String argumentName,Throwable e) {
    super(message, argumentName,e);
  }
}
