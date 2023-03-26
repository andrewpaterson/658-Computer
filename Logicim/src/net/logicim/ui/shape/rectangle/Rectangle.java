package net.logicim.ui.shape.rectangle;

import net.logicim.common.type.Tuple2;

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
}

