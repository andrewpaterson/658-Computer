package net.logicim.ui.shape.common;

import net.common.SimulatorException;
import net.common.type.Float2D;
import net.common.type.Int2D;
import net.common.type.Tuple2;
import net.logicim.data.integratedcircuit.common.BoundingBoxData;
import net.logicim.ui.common.Rotation;

import java.util.List;

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
    this(null, null);
  }

  public BoundingBox(float x1, float y1, float x2, float y2)
  {
    this();
    include(x1, y1);
    include(x2, y2);
  }

  public BoundingBox(Float2D topLeft, Float2D bottomRight)
  {
    this.topLeft = topLeft;
    this.bottomRight = bottomRight;
    transformedTopLeft = new Float2D();
    transformedBottomRight = new Float2D();

    if (topLeft != null && bottomRight != null)
    {
      if (topLeft.x > bottomRight.x)
      {
        throw new SimulatorException("Bounding box left [%s] must be less than right [%s].", topLeft.x, bottomRight.x);
      }
      if (topLeft.y > bottomRight.y)
      {
        throw new SimulatorException("Bounding box top [%s] must be less than bottom [%s].", topLeft.y, bottomRight.y);
      }
    }
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

  public Float2D getTopLeft()
  {
    return topLeft;
  }

  public Float2D getBottomRight()
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

  public Float2D getTransformedBottomRight()
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

  public boolean isNull()
  {
    return (topLeft == null) || (bottomRight == null);
  }

  public BoundingBoxData save()
  {
    return new BoundingBoxData(topLeft, bottomRight);
  }

  public void setTopLeft(Float2D topLeft)
  {
    if (this.topLeft == null)
    {
      this.topLeft = Float2D.safeClone(topLeft);
    }
    else
    {
      this.topLeft.set(topLeft);
    }
  }

  public void setBottomRight(Float2D bottomRight)
  {
    if (this.bottomRight == null)
    {
      this.bottomRight = Float2D.safeClone(bottomRight);
    }
    else
    {
      this.bottomRight.set(bottomRight);
    }
  }

  public static void calculateBoundingBox(BoundingBox boundingBox, List<ShapeView> shapes)
  {
    for (ShapeView shape : shapes)
    {
      if (shape != null)
      {
        shape.boundingBoxInclude(boundingBox);
      }
    }
  }

  public static void calculateBoundingBox(BoundingBox boundingBox, ShapeView... shapes)
  {
    for (ShapeView shape : shapes)
    {
      if (shape != null)
      {
        shape.boundingBoxInclude(boundingBox);
      }
    }
  }
}

