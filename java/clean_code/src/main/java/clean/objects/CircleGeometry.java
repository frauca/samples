package clean.objects;

public class CircleGeometry extends Geometry<Circle> implements GeometryShapeType{


  @Override
  public double area(Circle circle) {
    return circle.radius*circle.radius*Math.PI;
  }

  @Override
  public Class getTypeOfGeometryShape() {
    return Circle.class;
  }
}
