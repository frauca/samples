package clean.objects;

public class Square extends Rectangle {

  public final double side;

  public Square(double side) {
    super(/*widht=*/side,/*height=*/side);
    this.side = side;
  }
}
