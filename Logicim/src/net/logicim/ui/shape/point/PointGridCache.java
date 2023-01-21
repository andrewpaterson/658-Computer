package net.logicim.ui.shape.point;

import net.logicim.common.type.Tuple2;
import net.logicim.ui.common.Rotation;
import net.logicim.ui.shape.common.GridCache;

public class PointGridCache
    extends GridCache
{
  protected Tuple2 transformedPosition;

  public PointGridCache(Tuple2 point)
  {
    super();
    transformedPosition = point.clone();
  }

  public void update(Tuple2 point, Rotation rotation, Tuple2 position)
  {
    update();

    rotation.rotate(transformedPosition, point);

    transformedPosition.add(position);
  }

  public Tuple2 getTransformedPosition()
  {
    return transformedPosition;
  }
}

