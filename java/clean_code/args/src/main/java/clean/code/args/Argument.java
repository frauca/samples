package clean.code.args;

/**
 * Argument of type T
 * @param <T> type of te argument
 */
public class Argument<T> implements Comparable<Argument<T>> {

  protected final String name;
  protected final T value;

  public Argument(String name,T value){
    this.name = name;
    this.value = value;
  }

  public String getName(){
    return this.name;
  }

  public T getValue(){
    return this.value;
  }

  @Override
  public int compareTo(Argument<T> o) {
    return  getName().compareTo(o.getName());
  }
}
