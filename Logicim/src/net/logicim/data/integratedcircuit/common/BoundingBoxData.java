package net.logicim.data.integratedcircuit.common;

import net.common.type.Float2D;
import net.logicim.data.common.ReflectiveData;
import net.logicim.ui.shape.common.BoundingBox;

public class BoundingBoxData
    extends ReflectiveData
{
  protected Float2D topLeft;
  protected Float2D bottomRight;

  public BoundingBoxData()
  {
  }

  public BoundingBoxData(Float2D topLeft, Float2D bottomRight)
  {
    this.topLeft = topLeft;
    this.bottomRight = bottomRight;
  }

  public BoundingBox create()
  {
    return new BoundingBox(topLeft, bottomRight);
  }
}

