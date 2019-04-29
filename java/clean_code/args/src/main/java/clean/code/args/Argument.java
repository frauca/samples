package clean.code.args;

import java.util.Optional;

/**
 * The Argmunt is set empty on the parse of the configuration.
 * Then filled arguments are generated on the arguments process.
 * To make a new argument just implement one ArgumentClass
 * @see ArgumentsParser
 * Argument of type T
 * @param <T> type of te argument
 */
public abstract class Argument<T> {

  protected final String name;
  protected final Optional<T> value;

  public abstract Argument<T> parseValue(String value);
  public abstract Argument<T> parseConfiguration(String argumentName) throws clean.code.args.ArgumentNotFromMyTypeException;

  public String getName(){
    return this.name;
  }

  public T getValue(){
    return this.value
        .orElseThrow(()->
          new clean.code.args.ArgumentException(String.format("Argument %s is not setted",getName()),getName())
         );
  }

  public Argument(String name){
    this.name = name;
    this.value = Optional.empty();
  }

  public Argument(String name,T value){
    this.name = name;
    this.value = Optional.of(value);
  }
}
