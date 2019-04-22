package clean.objects;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class GeometryFactory {

  private Map<String,Geometry<?>> geometryByClassName;

  public Geometry findGeomertyForShape(Shape shape){
    String className = shape.getClass().getName();
    return geometryByClassName.get(className);
  }

  public GeometryFactory(){
    this.geometryByClassName = findAllGeometryOnClassPath();
  }

  private Map<String,Geometry<?>> findAllGeometryOnClassPath(){
    List<Geometry<?>> allGeometries = findAllGeometryOnClassPathInAList();
    return mapListByClass(allGeometries);
  }

  private List<Geometry<?>> findAllGeometryOnClassPathInAList() {
    //TODO make it with reflection
    return Arrays.asList(new CircleGeometry(),new RectangleGeometry());
  }

  private Map<String, Geometry<?>> mapListByClass(List<Geometry<?>> allGeometries) {
    return allGeometries.stream()
        .collect(
            Collectors.toMap(
                geometry->geometry.getClass().getName()
                ,geometry -> geometry
            )
        );
  }
}
