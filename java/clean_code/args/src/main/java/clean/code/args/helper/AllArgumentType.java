package clean.code.args.helper;

import clean.code.args.Argument;
import clean.code.args.ArgumentEndingTag;
import clean.code.args.ArgumentException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AllArgumentType {

  ClassFinder classFinder = new ClassFinder();

  public List<Argument> getAllArgumentTypes(){
    List<Class> allArgumentClasses = classFinder.getAllClasses("clean.code.args", Argument.class);
    Stream<Class> argumentWithoutBaseClass = allArgumentClasses.stream()
        .filter(this::filterArgumentBaseClass)
        .sorted(ensureTaggedGoFirst());
    return argumentWithoutBaseClass
        .map(this::instantiate)
        .collect(Collectors.toList());
  }

  private Comparator<Class> ensureTaggedGoFirst() {
    return (Class o1, Class o2) -> {
      Class ae = ArgumentEndingTag.class;
      if (ae.isAssignableFrom(o1)) {
        if (ae.isAssignableFrom(o2)) {
          return o1.getName().compareTo(o2.getName());
        } else {
          return -1;
        }
      } else {
        if (ae.isAssignableFrom(o2)) {
          return 1;
        } else {
          return o1.getName().compareTo(o2.getName());
        }
      }
    };
  }

  private boolean filterArgumentBaseClass(Class argumentClass){
    return !Modifier.isAbstract(argumentClass.getModifiers());
  }

  private Argument instantiate(Class argumentClass){
    try {
      return callToOneStringConstructor(argumentClass);
    } catch (IllegalAccessException|InvocationTargetException|InstantiationException|NoSuchMethodException e) {
     throw new ArgumentException(String.format("Could not instantiate %s",argumentClass),e);
    }
  }

  private Argument callToOneStringConstructor(Class argumeClass)
      throws IllegalAccessException, InvocationTargetException, InstantiationException, NoSuchMethodException {
    Constructor<Argument> c = argumeClass.getConstructor(String.class);
    return c.newInstance("forConfigurationArgument");
  }



}
