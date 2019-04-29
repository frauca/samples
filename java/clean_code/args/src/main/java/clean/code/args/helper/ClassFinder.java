package clean.code.args.helper;

import clean.code.args.ArgumentException;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.stream.Collectors;

public class ClassFinder {

  public List<Class> getAllClasses(String packagePath,Class type){
    List<Class> allPackageClasses = getAllClasses(packagePath);
    return allPackageClasses.stream()
        .filter(_class -> type.isAssignableFrom(_class))
        .collect(Collectors.toList());
  }

  public List<Class> getAllClasses(String packagePath){
    Enumeration<URL> resources = getAllURLOfPackage(packagePath);
    List<Class> classes = new ArrayList<>();
    for (File directory : convertURLsInFiles(resources)) {
      try {
        classes.addAll(findClasses(directory, packagePath));
      } catch (ClassNotFoundException e) {
        throw new ArgumentException(String.format("Could not get all classes from %s",packagePath),e);
      }
    }
    return classes;
  }

  /**
   * Recursive method used to find all classes in a given directory and subdirs.
   *
   * @param directory   The base directory
   * @param packageName The package name for classes found inside the base directory
   * @return The classes
   * @throws ClassNotFoundException
   */
  private static List findClasses(File directory, String packageName) throws ClassNotFoundException {
    List classes = new ArrayList();
    if (!directory.exists()) {
      return classes;
    }
    File[] files = directory.listFiles();
    for (File file : files) {
      if (file.isDirectory()) {
        assert !file.getName().contains(".");
        classes.addAll(findClasses(file, packageName + "." + file.getName()));
      } else if (file.getName().endsWith(".class")) {
        classes.add(Class.forName(packageName + '.' + file.getName().substring(0, file.getName().length() - 6)));
      }
    }
    return classes;
  }

  private Enumeration<URL> getAllURLOfPackage(String packagePath){
    try {
      ClassLoader classLoader = getClassLoader();
      return  classLoader.getResources(convertClassIntoDir(packagePath));
    }catch (IOException e){
      throw new ArgumentException(String.format("Could not find packages on %s",packagePath),e);
    }
  }

  private List<File> convertURLsInFiles(Enumeration<URL> resources){
    List dirs = new ArrayList();
    while (resources.hasMoreElements()) {
      URL resource = resources.nextElement();
      dirs.add(new File(resource.getFile()));
    }
    return dirs;
  }

  private ClassLoader getClassLoader(){
    ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
    assert classLoader != null;
    return classLoader;
  }

  private String convertClassIntoDir(String packagePath){
    return  packagePath.replace('.', '/');
  }


}
