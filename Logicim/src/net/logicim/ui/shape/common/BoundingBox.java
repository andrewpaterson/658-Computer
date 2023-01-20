package net.logicim.ui.shape.common;

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

  public static boolean containsPoint(Int2D position, Int2D boundBoxPosition, Int2D boundBoxDimension)
  {
    return ((position.x >= boundBoxPosition.x) &&
            (position.x <= boundBoxPosition.x + boundBoxDimension.x)) &&
           ((position.y >= boundBoxPosition.y) &&
            (position.y <= boundBoxPosition.y + boundBoxDimension.y));
  }

  public static boolean containsPoint(Int2D position, Float2D boundBoxPosition, Float2D boundBoxDimension)
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

  public static boolean containsBox(Float2D start, Float2D end, Float2D boundBoxPosition, Float2D boundBoxDimension, boolean includeIntersections)
  {
    float x1 = start.x;
    float x2 = end.x;
    float y1 = start.y;
    float y2 = end.y;
    if (x1 > x2)
    {
      float i = x2;
      x2 = x1;
      x1 = i;
    }
    if (y1 > y2)
    {
      float i = y2;
      y2 = y1;
      y1 = i;
    }

    if (!includeIntersections)
    {
      if ((x1 <= boundBoxPosition.x) && (y1 <= boundBoxPosition.y) &&
          (x2 >= boundBoxPosition.x + boundBoxDimension.x) && (y2 >= boundBoxPosition.y + boundBoxDimension.y))
      {
        return true;
      }
    }
    else
    {
      if ((x1 - boundBoxDimension.x <= boundBoxPosition.x) && (y1 - boundBoxDimension.y <= boundBoxPosition.y) &&
          (x2 >= boundBoxPosition.x) && (y2 >= boundBoxPosition.y))
      {
        return true;
      }
    }

    return false;
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
    if (topLeft != null)
    {
      topLeft.subtract(size, size);
    }
    if (bottomRight != null)
    {
      bottomRight.add(size, size);
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

  protected Float2D getTopLeft()
  {
    return topLeft;
  }

  protected Float2D getBottomRight()
  {
    return bottomRight;
  }

  public float getLeft()
  {
    return topLeft.x;
  }

  public float getRight()
  {
    return bottomRight.x;
  }

  protected Float2D getTransformedBottomRight()
  {
    return transformedBottomRight;
  }

  public void copy(BoundingBox source)
  {
    if (topLeft == null)
    {
      topLeft = Float2D.safeClone(source.getTopLeft());
    }
    else
    {
      this.topLeft.set(source.getTopLeft());
    }
    if (bottomRight == null)
    {
      bottomRight = Float2D.safeClone(source.getBottomRight());
    }
    else
    {
      this.bottomRight.set(source.getBottomRight());
    }
  }
}

