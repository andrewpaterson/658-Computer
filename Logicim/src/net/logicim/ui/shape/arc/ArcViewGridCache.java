package net.logicim.ui.shape.arc;

import net.logicim.common.type.Float2D;
import net.logicim.common.type.Tuple2;
import net.logicim.ui.common.Rotation;
import net.logicim.ui.shape.common.GridCache;

public class ArcViewGridCache
    extends GridCache
{
  protected Float2D circleCenter;
  protected int startAngle;
  protected int diameter;

  public ArcViewGridCache()
  {
    super();
    circleCenter = new Float2D();
  }

  public void update(Rotation rotation, Tuple2 position, Float2D circleCenter, float width, int startAngle)
  {
    update();

    rotation.rotate(this.circleCenter, circleCenter);

    this.circleCenter.add(position);
    this.circleCenter.subtract(width, width);
    int degrees = rotation.rotationToDegrees();
    this.startAngle = startAngle + degrees;
    this.diameter = (int) (width * 2.0f);
  }
}

