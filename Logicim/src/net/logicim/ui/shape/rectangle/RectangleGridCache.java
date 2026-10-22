package net.logicim.ui.shape.rectangle;

import net.common.type.Float2D;
import net.common.type.Int2D;
import net.common.type.Tuple2;
import net.logicim.ui.common.Rotation;
import net.logicim.ui.shape.common.GridCache;

public class RectangleGridCache
    extends GridCache
{
  protected Tuple2 transformedDimension;
  protected Tuple2 transformedPosition;

  public RectangleGridCache(Tuple2 position, Tuple2 dimension)
  {
    transformedPosition = position.clone();
    transformedDimension = Tuple2.safeClone(dimension);
  }

  public void update(Tuple2 positionRelativeToIC, Tuple2 dimension, Rotation rotation, Tuple2 position)
  {
    update();

    rotation.rotate(transformedPosition, positionRelativeToIC);

    if (dimension != null)
    {
      if (transformedDimension == null)
      {
        transformedDimension = Tuple2.safeClone(dimension);
      }

      rotation.rotate(transformedDimension, dimension);
      if (transformedDimension instanceof Int2D)
      {
        transformInt2D(position);
      }
      if (transformedDimension instanceof Float2D)
      {
        transformFloat2D(position);
      }
    }
  }

  private void transformFloat2D(Tuple2 position)
  {
    float x = 0;
    float y = 0;
    Float2D float2D = (Float2D) this.transformedDimension;
    float width = float2D.x;
    float height = float2D.y;
    if (width < 0)
    {
      x += width;
      width *= -1;
    }
    if (height < 0)
    {
      y += height;
      height *= -1;
    }
    transformedDimension.set(width, height);
    transformedPosition.add(x, y);
    transformedPosition.add(position);
  }

  private void transformInt2D(Tuple2 position)
  {
    int x = 0;
    int y = 0;
    Int2D int2D = (Int2D) this.transformedDimension;
    int width = int2D.x;
    int height = int2D.y;
    if (width < 0)
    {
      x += width;
      width *= -1;
    }
    if (height < 0)
    {
      y += height;
      height *= -1;
    }
    transformedDimension.set(width, height);
    transformedPosition.add(x, y);
    transformedPosition.add(position);
  }

  public Tuple2 getTransformedDimension()
  {
    return transformedDimension;
  }

  public Tuple2 getTransformedPosition()
  {
    return transformedPosition;
  }
}

