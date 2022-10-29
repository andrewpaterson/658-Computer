package net.logicim.ui.shape;

import net.logicim.common.type.Float2D;
import net.logicim.common.type.Tuple2;
import net.logicim.ui.common.Rotation;

public class BoundingBox
{
  protected Float2D topLeft;
  protected Float2D bottomRight;

  protected Float2D transformedTopLeft;
  protected Float2D transformedBottomRight;

  protected float transformedWidth;
  protected float transformedHeight;

  public BoundingBox()
  {
    topLeft = null;
    bottomRight = null;
    transformedTopLeft = new Float2D();
    transformedBottomRight = new Float2D();
  }

  public void include(Tuple2 tuple)
  {
    include(tuple.getX(), tuple.getY());
  }

  public void include(float x, float y)
  {
    if (topLeft == null)
    {
      topLeft = new Float2D(x, y);
    }
    else
    {
      if (topLeft.x > x)
      {
        topLeft.x = x;
      }
      if (topLeft.y > y)
      {
        topLeft.y = y;
      }
    }

    if (bottomRight == null)
    {
      bottomRight = new Float2D(x, y);
    }
    else
    {
      if (bottomRight.x < x)
      {
        bottomRight.x = x;
      }
      if (bottomRight.y < y)
      {
        bottomRight.y = y;
      }
    }
  }

  public void transform(Rotation r)
  {
    transformedTopLeft.set(topLeft);
    transformedBottomRight.set(bottomRight);

    r.transform(transformedTopLeft);
    r.transform(transformedBottomRight);

    transformedWidth = transformedBottomRight.x - transformedTopLeft.x;
    transformedHeight = transformedBottomRight.y - transformedTopLeft.y;
  }

  public Float2D getTransformedTopLeft()
  {
    return transformedTopLeft;
  }

  public float getTransformedWidth()
  {
    return transformedWidth;
  }

  public float getTransformedHeight()
  {
    return transformedHeight;
  }

  public void grow(float size)
  {
    topLeft.subtract(size, size);
    bottomRight.add(size, size);
  }
}

