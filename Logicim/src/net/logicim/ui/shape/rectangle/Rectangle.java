package net.logicim.ui.shape.rectangle;

import net.common.type.Tuple1;
import net.common.type.Tuple2;
import net.logicim.ui.common.Rotation;

public class Rectangle
{
  protected Tuple2 topLeft;
  protected Tuple2 bottomRight;

  public Rectangle(Tuple2 topLeft, Tuple2 bottomRight)
  {
    this.topLeft = topLeft;
    this.bottomRight = bottomRight;
  }

  public Tuple2 getTopLeft()
  {
    return topLeft;
  }

  public Tuple2 getBottomRight()
  {
    return bottomRight;
  }

  public Tuple2 getDimension()
  {
    Tuple2 dimension = bottomRight.clone();
    dimension.subtract(topLeft);
    return dimension;
  }

  public void rotateRight()
  {
    Rotation.West.transform(topLeft);
    Rotation.West.transform(bottomRight);

    Tuple1 x1 = topLeft.getXTuple();
    Tuple1 x2 = bottomRight.getXTuple();
    if (x2.lessThan(x1))
    {
      topLeft.setX(x2);
      bottomRight.setX(x1);
    }
    Tuple1 y1 = topLeft.getYTuple();
    Tuple1 y2 = bottomRight.getYTuple();
    if (y2.lessThan(y1))
    {
      topLeft.setY(y2);
      bottomRight.setY(y1);
    }
  }
}

