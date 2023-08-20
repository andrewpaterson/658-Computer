package net.logicim.ui.shape.arc;

import net.common.type.Float2D;
import net.common.type.Tuple2;
import net.logicim.ui.common.Rotation;
import net.logicim.ui.shape.common.GridCache;

public class ArcViewGridCache
    extends GridCache
{
  protected Float2D circleCenter;
  protected int startAngle;
  protected int widthDiameter;
  protected int heightDiameter;

  public ArcViewGridCache()
  {
    super();
    circleCenter = new Float2D();
  }

  public void update(Rotation rotation, Tuple2 position, Float2D circleCenter, float width, float height, int startAngle)
  {
    update();

    rotation.rotate(this.circleCenter, circleCenter);

    if (rotation.isEastWest())
    {
      float temp = width;
      width = height;
      height = temp;
    }

    this.circleCenter.add(position);
    this.circleCenter.subtract(width, height);
    int degrees = rotation.rotationToDegrees();
    this.startAngle = startAngle + degrees;
    this.widthDiameter = (int) (width * 2.0f);
    this.heightDiameter = (int) (height * 2.0f);
  }
}

