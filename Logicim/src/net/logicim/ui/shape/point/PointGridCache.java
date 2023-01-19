package net.logicim.ui.shape.point;

import net.logicim.common.type.Tuple2;
import net.logicim.ui.common.Rotation;
import net.logicim.ui.shape.common.GridCache;

public class PointGridCache
    extends GridCache
{
  protected Tuple2 transformed;

  public PointGridCache(Tuple2 point)
  {
    super();
    transformed = point.clone();
  }

  public void update(Tuple2 point, Rotation rotation, Tuple2 position)
  {
    update();

    rotation.rotate(transformed, point);

    transformed.add(position);
  }

  public Tuple2 getTransformed()
  {
    return transformed;
  }
}

