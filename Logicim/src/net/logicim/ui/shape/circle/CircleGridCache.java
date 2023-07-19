package net.logicim.ui.shape.circle;

import net.logicim.common.type.Float2D;
import net.logicim.common.type.Tuple2;
import net.logicim.ui.common.Rotation;
import net.logicim.ui.shape.common.GridCache;

public class CircleGridCache
    extends GridCache
{
  protected Float2D transformedCircleCenter;
  protected int transformedWidthDiameter;
  protected int transformedHeightDiameter;

  public CircleGridCache(Float2D circleCenter,
                         float width,
                         float height)
  {
    this.transformedCircleCenter = circleCenter.clone();
    this.transformedWidthDiameter = (int) (width * 2.0f);
    this.transformedHeightDiameter = (int) (height * 2.0f);
  }

  @SuppressWarnings("SuspiciousNameCombination")
  public void update(Float2D circleCenter,
                     float width,
                     float height,
                     Rotation rotation,
                     Tuple2 position)
  {
    update();

    rotation.rotate(this.transformedCircleCenter, circleCenter);

    if (rotation.isEastWest())
    {
      float temp = width;
      width = height;
      height = temp;
    }

    this.transformedCircleCenter.add(position);
    this.transformedCircleCenter.subtract(width, height);
    this.transformedWidthDiameter = (int) (width * 2.0f);
    this.transformedHeightDiameter = (int) (height * 2.0f);
  }

  public Float2D getTransformedCircleCenter()
  {
    return transformedCircleCenter;
  }

  public int getTransformedWidthDiameter()
  {
    return transformedWidthDiameter;
  }

  public int getTransformedHeightDiameter()
  {
    return transformedHeightDiameter;
  }
}

