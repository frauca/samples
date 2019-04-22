package clean.objects;

public class RectangleGeometry extends Geometry<Rectangle> implements GeometryShapeType {

  @Override
  public double area(Rectangle rectangle) {
    return rectangle.height*rectangle.width;
  }

  @Override
  public Class getTypeOfGeometryShape() {
    return Rectangle.class;
  }
}
