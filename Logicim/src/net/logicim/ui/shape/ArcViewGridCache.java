package net.logicim.ui.shape;

import net.logicim.common.type.Float2D;
import net.logicim.common.type.Tuple2;
import net.logicim.ui.common.Rotation;

public class ArcViewGridCache
{
  protected Float2D circleCenter;
  protected boolean valid;
  protected int startAngle;
  protected int diameter;

  public ArcViewGridCache()
  {
    circleCenter = new Float2D();
    valid = false;
  }

  public boolean isValid()
  {
    return valid;
  }

  public void update(Rotation rotation, Tuple2 position, Float2D circleCenter, float width, int startAngle)
  {
    valid = true;

    rotation.rotate(this.circleCenter, circleCenter);

    this.circleCenter.add(position);
    this.circleCenter.subtract(width, width);
    int degrees = rotation.rotationToDegrees();
    this.startAngle = startAngle + degrees;
    this.diameter = (int) (width * 2.0f);
  }

  public void invalidate()
  {
    valid = false;
  }
}

