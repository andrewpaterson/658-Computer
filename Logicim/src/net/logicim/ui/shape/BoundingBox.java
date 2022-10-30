package net.logicim.ui.shape;

import net.logicim.common.type.Float2D;
import net.logicim.common.type.Int2D;
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

  public static boolean isContained(Int2D position, Int2D boundBoxPosition, Int2D boundBoxDimension)
  {
    return ((position.x >= boundBoxPosition.x) &&
            (position.x <= boundBoxPosition.x + boundBoxDimension.x)) &&
           ((position.y >= boundBoxPosition.y) &&
            (position.y <= boundBoxPosition.y + boundBoxDimension.y));
  }

  public static boolean isContained(Int2D position, Float2D boundBoxPosition, Float2D boundBoxDimension)
  {
    return ((position.x >= boundBoxPosition.x) &&
            (position.x <= boundBoxPosition.x + boundBoxDimension.x)) &&
           ((position.y >= boundBoxPosition.y) &&
            (position.y <= boundBoxPosition.y + boundBoxDimension.y));
  }

  public static float calculateDistance(Int2D position1, Int2D position2)
  {
    int x = position2.x - position1.x;
    int y = position2.y - position1.y;

    x *= x;
    y *= y;

    return (float) Math.sqrt(x + y);
  }

  public void include(Tuple2 tuple)
  {
    include(tuple.getX(), tuple.getY(), 0);

  }

  public void include(Tuple2 tuple, float radius)
  {
    include(tuple.getX(), tuple.getY(), radius);
  }

  public void include(float x, float y)
  {
    include(x, y, 0);
  }

  public void include(float x, float y, float radius)
  {
    if (topLeft == null)
    {
      topLeft = new Float2D(x - radius, y - radius);
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
      bottomRight = new Float2D(x + radius, y + radius);
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

  public void grow(float size)
  {
    topLeft.subtract(size, size);
    bottomRight.add(size, size);
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
}

