package net.logicim.ui.shape;

import net.logicim.common.type.Float2D;
import net.logicim.common.type.Int2D;
import net.logicim.common.type.Tuple2;
import net.logicim.ui.common.Rotation;

public class RectangleGridCache
{
  protected Tuple2 transformedDimension;
  protected Tuple2 transformedPosition;
  protected boolean valid;

  public RectangleGridCache(Tuple2 dimension, Tuple2 position)
  {
    transformedPosition = position.clone();
    transformedDimension = dimension.clone();
    valid = false;
  }

  public boolean isValid()
  {
    return valid;
  }

  public void invalidate()
  {
    valid = false;
  }

  public void update(Tuple2 dimension, Tuple2 positionRelativeToIC, Rotation rotation, Tuple2 position)
  {
    valid = true;
    rotation.rotate(transformedPosition, positionRelativeToIC);
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

