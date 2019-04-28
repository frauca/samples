package clean.code.args;

import java.util.Optional;

public class ArgumentException extends RuntimeException {
  private final Optional<String> argumentName;

  public ArgumentException(String message){
    super(message);
    argumentName = Optional.empty();
  }

  public ArgumentException(String message,Throwable cause){
    super(message,cause);
    argumentName = Optional.empty();
  }

  public ArgumentException(String message,String argumentName){
    super(message);
    this.argumentName=Optional.of(argumentName);
  }

  public ArgumentException(String message,String argumentName,Throwable e){
    super(message,e);
    this.argumentName=Optional.of(argumentName);
  }

  public Optional<String> getArgumentName() {
    return argumentName;
  }
}
